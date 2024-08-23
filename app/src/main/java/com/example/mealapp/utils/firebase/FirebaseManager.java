package com.example.mealapp.utils.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealapp.R;
import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.User;
import com.example.mealapp.utils.constants.ConstantKeys;
import com.example.mealapp.utils.resource_helper.ResourceHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseManager {

    private static FirebaseManager instance;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    private static final String TAG = "FirebaseManager";

    private FirebaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    //Authentication
    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public void signInWithGoogle(GoogleSignInAccount account, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(onCompleteListener);
    }

    public void signUp(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            saveUserData(new User(email, name));
                        }
                    }
                    onCompleteListener.onComplete(task);
                });
    }

    public void signIn(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public void getCurrentUser(@NonNull OnUserRetrieveData listener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firestore.collection(ConstantKeys.COLLECTION_USERS)
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString(ConstantKeys.KEY_NAME);
                                String email = document.getString(ConstantKeys.KEY_EMAIL);
                                User user = new User(email, name, userId);
                                listener.onUserDataRetrieved(user);
                            } else {
                                listener.onError(new Exception(ResourceHelper.getString(R.string.error_no_user_data)));
                            }
                        } else {
                            listener.onError(task.getException());
                        }
                    });
        } else {
            listener.onError(new Exception(ResourceHelper.getString(R.string.error_no_current_user)));
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public void saveUserData(User user) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            firestore.collection(ConstantKeys.COLLECTION_USERS)
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener(s -> Log.d(TAG, "User data saved successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error saving user data", e));
        }
    }


    //BackUp handling

    private void deleteFavoriteMeals(@NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference collectionRef = firestore.collection(ConstantKeys.COLLECTION_FAVORITE_MEALS);
            //Filter docs
            Query query = collectionRef.whereEqualTo(ConstantKeys.KEY_ID_USER, userId);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteTasks.add(document.getReference().delete());
                    }

                    Tasks.whenAll(deleteTasks).addOnCompleteListener(onCompleteListener);
                } else {
                    onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                }
            });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }

    public void deleteMealPlans(@NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference collectionRef = firestore.collection(ConstantKeys.COLLECTION_PLAN_MEALS);

            Query query = collectionRef.whereEqualTo(ConstantKeys.KEY_ID_USER, userId);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteTasks.add(document.getReference().delete());
                    }

                    Tasks.whenAll(deleteTasks).addOnCompleteListener(onCompleteListener);
                } else {
                    onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                }
            });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }

    public void deleteMealIngredients(@NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference collectionRef = firestore.collection(ConstantKeys.COLLECTION_INGREDIENTS);

            Query query = collectionRef.whereEqualTo(ConstantKeys.KEY_ID_USER, userId);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Task<Void>> deleteTasks = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        deleteTasks.add(document.getReference().delete());
                    }

                    Tasks.whenAll(deleteTasks).addOnCompleteListener(onCompleteListener);
                } else {
                    onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                }
            });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }


    public void setFavoriteMeals(List<FavoriteMeal> meals, @NonNull OnCompleteListener<Void> onCompleteListener) {
        deleteFavoriteMeals(deleteTask -> {
            if (deleteTask.isSuccessful()) {
                List<Task<Void>> tasks = new ArrayList<>();
                for (FavoriteMeal meal : meals) {
                    DocumentReference docRef = firestore.collection(ConstantKeys.COLLECTION_FAVORITE_MEALS)
                            .document(meal.getIdMeal() + meal.getIdUser() + "favorite");
                    Task<Void> task = docRef.set(meal.toMap())
                            .addOnSuccessListener(suc -> Log.d(TAG, "Favorite meal saved successfully: " + meal.getStrMeal()))
                            .addOnFailureListener(e -> Log.e(TAG, "Error saving favorite meal: " + meal.getStrMeal()));
                    tasks.add(task);
                }
                Tasks.whenAll(tasks).addOnCompleteListener(onCompleteListener);
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(deleteTask.getException())));
            }
        });
    }

    public void setMealPlans(List<MealPlan> plans, @NonNull OnCompleteListener<Void> onCompleteListener) {
        deleteMealPlans(deleteTask -> {
            if (deleteTask.isSuccessful()) {
                List<Task<Void>> tasks = new ArrayList<>();
                for (MealPlan meal : plans) {
                    DocumentReference docRef = firestore.collection(ConstantKeys.COLLECTION_PLAN_MEALS)
                            .document(meal.getIdMeal() + meal.getIdUser() + meal.getDateCreated() +"plan");
                    Task<Void> task = docRef.set(meal.toMap())
                            .addOnSuccessListener(suc -> Log.d(TAG, "Plan meal saved successfully: " + meal.getStrMeal()))
                            .addOnFailureListener(e -> Log.e(TAG, "Error saving plan meal: " + meal.getStrMeal()));
                    tasks.add(task);
                }
                Tasks.whenAll(tasks).addOnCompleteListener(onCompleteListener);
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(deleteTask.getException())));
            }
        });
    }

    public void setMealIngredients(List<MealIngredient> ingredients, @NonNull OnCompleteListener<Void> onCompleteListener) {
        deleteMealIngredients(deleteTask -> {
            if (deleteTask.isSuccessful()) {
                List<Task<Void>> tasks = new ArrayList<>();
                for (MealIngredient ingredient : ingredients) {
                    DocumentReference docRef = firestore.collection(ConstantKeys.COLLECTION_INGREDIENTS)
                            .document(ingredient.getIngredientName() + ingredient.getIdUser() + "ingredient");
                    Task<Void> task = docRef.set(ingredient.toMap())
                            .addOnSuccessListener(suc -> Log.d(TAG, "Ingredient saved successfully: " + ingredient.getIngredientName()))
                            .addOnFailureListener(e -> Log.e(TAG, "Error saving plan meal: " + ingredient.getIngredientName()));
                    tasks.add(task);
                }
                Tasks.whenAll(tasks).addOnCompleteListener(onCompleteListener);
            } else {
                onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(deleteTask.getException())));
            }
        });
    }

    public void getFavoriteMeals(String userId, @NonNull OnCompleteListener<List<FavoriteMeal>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection(ConstantKeys.COLLECTION_FAVORITE_MEALS)
                    .whereEqualTo(ConstantKeys.KEY_ID_USER, userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<FavoriteMeal> favoriteMeals = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FavoriteMeal meal = FavoriteMeal.fromMap(document.getData());
                                favoriteMeals.add(meal);
                            }
                            if(!favoriteMeals.isEmpty()){
                                onCompleteListener.onComplete(Tasks.forResult(favoriteMeals));

                            }else{
                               onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.no_favorite_meals_found))));
                            }
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }

    public void getMealPlans(String userId, @NonNull OnCompleteListener<List<MealPlan>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection(ConstantKeys.COLLECTION_PLAN_MEALS)
                    .whereEqualTo(ConstantKeys.KEY_ID_USER, userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<MealPlan> mealPlans = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MealPlan plan = MealPlan.fromMap(document.getData());
                                mealPlans.add(plan);
                            }
                            if(!mealPlans.isEmpty()) {
                                onCompleteListener.onComplete(Tasks.forResult(mealPlans));
                            }else{
                                onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.no_meal_plans_found))));
                            }
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }


    public void getMealIngredients(String userId, @NonNull OnCompleteListener<List<MealIngredient>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection(ConstantKeys.COLLECTION_INGREDIENTS)
                    .whereEqualTo(ConstantKeys.KEY_ID_USER, userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<MealIngredient> mealIngredients = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MealIngredient ingredient = MealIngredient.fromMap(document.getData());
                                mealIngredients.add(ingredient);
                            }

                            if (!mealIngredients.isEmpty()) {
                                onCompleteListener.onComplete(Tasks.forResult(mealIngredients));
                            } else {
                                onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_ingredients))));
                            }
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception(ResourceHelper.getString(R.string.error_no_current_user))));
        }
    }


}
