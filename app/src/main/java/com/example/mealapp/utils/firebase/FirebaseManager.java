package com.example.mealapp.utils.firebase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealapp.utils.common_layer.local_models.FavoriteMeal;
import com.example.mealapp.utils.common_layer.local_models.MealIngredient;
import com.example.mealapp.utils.common_layer.local_models.MealPlan;
import com.example.mealapp.utils.common_layer.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = document.getString("email");
                                User user = new User(email, name, userId);
                                listener.onUserDataRetrieved(user);
                            } else {
                                listener.onError(new Exception("No user data found"));
                            }
                        } else {
                            listener.onError(task.getException());
                        }
                    });
        } else {
            listener.onError(new Exception("No current user"));
        }
    }
    public void signOut() {
        firebaseAuth.signOut();
    }
    public void saveUserData(User user) {
        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            firestore.collection("users")
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener(s -> Log.d(TAG, "User data saved successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error saving user data", e));
        }
    }
    public void getUserData(@NonNull final OnUserRetrieveData listener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                String email = document.getString("email");
                                User user = new User(email, name , userId);
                                listener.onUserDataRetrieved(user);
                            } else {
                                listener.onError(new Exception("No user data found"));
                            }
                        } else {
                            listener.onError(task.getException());
                        }
                    });
        } else {
            listener.onError(new Exception("No current user"));
        }
    }

    //BackUp handling

    public void setFavoriteMeals(List<FavoriteMeal> meals, @NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        List<Task<Void>> tasks = new ArrayList<>();

        if (currentUser != null ) {
            for (FavoriteMeal meal : meals) {
                DocumentReference docRef = firestore.collection("favorite_meals")
                        .document(meal.getIdMeal() + meal.getIdUser() + "favorite");

                Task<Void> task = docRef.set(meal.toMap())
                        .addOnSuccessListener(suc -> Log.d(TAG, "Favorite meal saved successfully: " + meal.getStrMeal()))
                        .addOnFailureListener(e -> Log.e(TAG, "Error saving favorite meal: " + meal.getStrMeal()));

                tasks.add(task);
            }

            Task<Void> allTasks = Tasks.whenAll(tasks);
            allTasks.addOnCompleteListener(onCompleteListener);
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }

    public void setMealPlans(List<MealPlan> plans, @NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        List<Task<Void>> tasks = new ArrayList<>();

        if (currentUser != null) {
            for (MealPlan meal : plans) {
                DocumentReference docRef = firestore.collection("plan_meals")
                        .document(meal.getIdMeal() + meal.getIdUser() + "plan");

                Task<Void> task = docRef.set(meal.toMap())
                        .addOnSuccessListener(suc -> Log.d(TAG, "Plan meal saved successfully: " + meal.getStrMeal()))
                        .addOnFailureListener(e -> Log.e(TAG, "Error saving plan meal: " + meal.getStrMeal()));

                tasks.add(task);
            }

            Task<Void> allTasks = Tasks.whenAll(tasks);
            allTasks.addOnCompleteListener(onCompleteListener);
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }

    public void setMealIngredients(List<MealIngredient> ingredients, @NonNull OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        List<Task<Void>> tasks = new ArrayList<>();

        if (currentUser != null) {
            for (MealIngredient ingredient : ingredients) {
                DocumentReference docRef = firestore.collection("ingredients")
                        .document(ingredient.getIngredientName() + ingredient.getUserId() + "ingredient");

                Task<Void> task = docRef.set(ingredient.toMap())
                        .addOnSuccessListener(suc -> Log.d(TAG, "Ingredient saved successfully: " + ingredient.getIngredientName()))
                        .addOnFailureListener(e -> Log.e(TAG, "Error saving plan meal: " + ingredient.getIngredientName()));

                tasks.add(task);
            }

            Task<Void> allTasks = Tasks.whenAll(tasks);
            allTasks.addOnCompleteListener(onCompleteListener);
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }

    public void getFavoriteMeals(String userId, @NonNull OnCompleteListener<List<FavoriteMeal>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection("favorite_meals")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<FavoriteMeal> favoriteMeals = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FavoriteMeal meal = FavoriteMeal.fromMap(document.getData());
                                favoriteMeals.add(meal);
                            }
                            onCompleteListener.onComplete(Tasks.forResult(favoriteMeals));
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }

    public void getMealPlans(String userId, @NonNull OnCompleteListener<List<MealPlan>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection("plan_meals")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<MealPlan> mealPlans = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MealPlan plan = MealPlan.fromMap(document.getData());
                                mealPlans.add(plan);
                            }
                            onCompleteListener.onComplete(Tasks.forResult(mealPlans));
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }


    public void getMealIngredients(String userId, @NonNull OnCompleteListener<List<MealIngredient>> onCompleteListener) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            firestore.collection("ingredients")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<MealIngredient> mealIngredients = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MealIngredient ingredient = MealIngredient.fromMap(document.getData());
                                mealIngredients.add(ingredient);
                            }
                            onCompleteListener.onComplete(Tasks.forResult(mealIngredients));
                        } else {
                            onCompleteListener.onComplete(Tasks.forException(Objects.requireNonNull(task.getException())));
                        }
                    });
        } else {
            onCompleteListener.onComplete(Tasks.forException(new Exception("No current user")));
        }
    }



}
