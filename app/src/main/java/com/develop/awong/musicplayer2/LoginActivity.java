package com.develop.awong.musicplayer2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    public static List<Person> friends;
    public static String personName = "Unknown person name";
    public static String personEmail = "Unknown person email";
    private String personId;
    private String authCode;
    private Uri personPhoto;
    private PeopleService peopleService;


//    private String clientId = "352347582721-ou5ghfkkn03kdr517tv2kgphmo4qe04o.apps.googleusercontent.com";
//    private String clientSecret = "vMcqwb-uEWYy--t9jnj0mSZ6";
    private String clientId = "180165515834-qkqu4hs4hj7njetp886pdhtnb5pa7gq5.apps.googleusercontent.com";
    private String clientSecret = "co0VtfENp2w3xAefQ1tAAtqG";

    private String scope = "https://www.googleapis.com/auth/contacts.readonly";
    private GoogleSignInAccount account;
    private SignInButton signInButton;
    private Button signOutButton;
    private Button menuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        menuButton = (Button) findViewById(R.id.menu_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(clientId)
                .requestScopes(new Scope(scope))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);

        updateUI(account);


        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MenuActivity.flag = false;
                signIn();

                System.out.println("before running task authcode:" + authCode);
            }
        });


        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signOut();
                signInButton.setVisibility(View.VISIBLE);
            }
        });


        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                if (account == null) {
//                    Toast.makeText(LoginActivity.this,"You are not logged in", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    startMenuActivity();
//                }
                startMenuActivity();
            }
        });

    }
    /*
    @Override
    protected void onStart()
    {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        updateUI(account);

    }
    */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            System.out.println("i am here at onactivity result");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            System.out.println("Get result failed!");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            System.out.println("Successfully get sign in reuslt");

            account = completedTask.getResult(ApiException.class);
            updateUI(account);
            // Signed in successfully, show authenticated UI.
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(authCode);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("error","signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount acct)  {

        if (acct != null) {
            Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_LONG).show();

            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            if ( acct.getServerAuthCode() != null){
                //Toast.makeText(LoginActivity.this, "Fetch Autocode successful", Toast.LENGTH_LONG).show();
                authCode = acct.getServerAuthCode();
            }
            Uri personPhoto = acct.getPhotoUrl();
            System.out.println("name:  " + personName);
            System.out.println("email:  " + personEmail);
            System.out.println("autoCode:  " + authCode);
            signInButton.setVisibility(View.GONE);
            menuButton.setVisibility(View.VISIBLE);

        }
        else {
            System.out.println("account in null");
        }


    }

    public void peopleAPISetUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        String redirectUrl = "";


        // Step 2: Exchange -->
        System.out.println("auto code: " + authCode);
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, authCode, redirectUrl)
                        .execute();
        // End of Step 2 <--

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        peopleService =
                new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
        System.out.println("used people api!!!");

    }

    public void fetchConnections() throws IOException {
        ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                .setPersonFields("names,emailAddresses,phoneNumbers")
                .execute();
        List<Person> connections = response.getConnections();

        if (connections != null) {
            friends = new ArrayList<>();
            for (Person person : connections) {
                if (!person.isEmpty()) {
                    friends.add(person);
                    List<PhoneNumber> phoneNumbers = person.getPhoneNumbers();
                    List<Name> names = person.getNames();
                    List<EmailAddress> emails = person.getEmailAddresses();
                    System.out.println("names size" + names.size());
                    if (names != null)
                        for (Name name : names)
                            System.out.println("my friend name:  " + name.getDisplayName());
                    if (emails != null)
                        for (EmailAddress email : emails)
                            System.out.println("my friend email:  " + email.getValue());

                }
            }
        }
        else {
            //Looper.prepare();
            Toast.makeText(LoginActivity.this, "No friends found", Toast.LENGTH_LONG).show();
            //signOut();
            //signInButton.setVisibility(View.VISIBLE);
        }

    }

    private void signOut() {
        if (account == null) {
            Toast.makeText(LoginActivity.this,"You Are Not Logged In OR You Dont Have Friends :(", Toast.LENGTH_LONG).show();
        }
        else {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(LoginActivity.this, "Log out successful", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        account = null;
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("task started");
            System.out.println("strings[0] here: " + strings[0]);


            try {
                System.out.println("start people api setup");
                peopleAPISetUp();
                System.out.println("setting up");
                fetchConnections();
                System.out.println("after fetch");
            } catch (IOException e) {
                System.out.println("exception");
                e.printStackTrace();
                return "fail";
            }


            return "success";
        }
    }

    private void startMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
    }


}
