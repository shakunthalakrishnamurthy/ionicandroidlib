# ionicandroidlib

Guidelines to integrate this library into a client application:

Library:
1. Build the library file by "Make Module" under "Build" option.
2. .aar is generated in \build\outputs\aar folder of the project.


Client Application[Android]
1. Create an android project and create \libs folder under app folder.
2. Copy the generated .aar and paste it under the client app's libs folder along with cordova.jar
3. Under app's build.gradle:
          android{
          ...
           repositories {
           flatDir {
            dirs 'libs'
           }
          }
         } 
      
        dependencies {
        implementation fileTree(dir: 'libs', include: ['*.jar'])
        implementation(name: '<library-name>', ext: 'aar')
        implementation(name: 'cordova', ext: 'jar')
        }
4. Under MainActivity of the app, write the code for navigation from Android -> Hybrid SDK code:
  
        public class MainActivity extends AppCompatActivity {


          @Override
          protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_main);

              loadMishiPaySite();
          }

          /**
          * Loads the site in home page just for the UI experience
          **/
          private void loadMishiPaySite() {
              WebView webViewHome = findViewById(R.id.webViewHomePage);
              webViewHome.loadUrl("https://mishipay.com/");
          }

          /**
          * This method sends the user to profile page of hybrid sdk.
          **/
          public void goToProfilePage(View view){
              Intent intent = intent = new Intent(this, io.ionic.starter.MainActivity.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              this.startActivity(intent);
          }
        }
5. Alright! Its done.      
      
