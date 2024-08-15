package com.example.mealapp.utils.firebase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealapp.utils.common_layer.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {

    private static FirebaseManager instance;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    private static final String TAG = "FirebaseManager";
    private FirebaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

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
                                User user = new User(email, name);
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
                                User user = new User(email, name);
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
}
