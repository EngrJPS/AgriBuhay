package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RegistrationRetailer extends AppCompatActivity {

    //SPINNER
    //PROVINCE - CITY/MUNICIPALITY
    String [] Davao_de_Oro = {"Compostela", "Laak", "Mabini", "Maco", "Maragusan", "Mawab"
            , "Monkayo", "Montivista", "Nabunturan", "New Bataan", "Pantukan"};
    String [] Davao_del_Sur = {"Bansalan", "Davao City", "Digos", "Hagonoy", "Kiblawan"
            , "Magsaysay", "Malalag", "Matanao", "Padada", "Santa Cruz", "Sulop"};
    String [] Davao_del_Norte = {"Asuncion", "Baraulio E. Dujali", "Carmen", "Kapalong", "New Corella"
            , "Panabo", "Samal", "San Isidro", "Santo Tomas", "Tagum", "Talaingod"};
    String [] Davao_Oriental = {"Baganga", "Banaybanay", "Boston", "Caraga", "Cateel"
            , "Governor Generoso", "Lupon", "Manay", "Mati", "San Isidro (Oriental)"};
    String [] Davao_Occidental = {"Don Marcelino", "Jose Abad Santos", "Malita", "Santa Maria", "Sarangani"};
    //CITY/MUNICIPALITY-BARANGGAY
    //davao de oro
    String [] Compostela = {"Bagongon", "Gabi", "Lagab", "Mangayon", "Mapaca", "Maparat", "New Alegria", "Ngan", "Osme??a", "Panansalan", "Poblacion", "San Jose"};
    String [] Laak = {"Aguinaldo","Ampawid","Anitap","Banbanon","Binasbas","Ceboleda","Datu Ampunan", "Do??a Josefa","Il Papa","Inakayan","Kapatagan","Kidawa","Kiokmay"};
    String [] Mabini = {"Cadunan","Pindasan","Cuambog","Tagnanan (Mampising)","Anitapan","Cabuyuan","Del Pilar","Libodon","Golden Valley (Maraut)","Pangibiran", "San Antonio"};
    //Research scope area
    String [] Maco = {"Anibongan", "Anislagan", "Binuangan", "Bucana", "Calabcab", "Concepcion", "Dumlan", "Elizalde (Somil)", "Pangi (Gaudencio Antonio)"
            , "Gubatan", "Hijo", "Kinuban", "Langgam", "Lapu-lapu", "Libay-libay", "Limbo", "Lumatab", "Magangit", "Malamodao", "Manipongol"
            , "Mapaang", "Masara", "New Asturias", "Panibasan", "Panoraon", "San Juan", "San Roque", "Sangab", "Taglawig"
            , "Mainit", "New Barili", "New Leyte", "New Visayas", "Panangan", "Tagbaros", "Teresa"};
    String [] Maragusan = {"Bagong Silang","Mapawa","New Albay","Tupaz","Bahi","Cambagang","Coronobe","Katipunan","Lahi","Langgawisan","Mabuganao","Magcagong"};
    String [] Mawab = {"Andili","Bawani","Concepcion","Malinawon","Nueva Visayas","Nuevo Iloco","Poblacion","Salvacion","Saosao","Sawangan","Tuboran"};
    String [] Monkayo = {"Awao","Babag","Banlag","Baylo","Casoon","Inambatan","Haguimitan","Macopo","Mamunga","Naboc","Olaycon","Salvacion"};
    String [] Montivista = {"Banagbanag","Banglasan","Bankerohan Norte","Bankerohan Sur","Camansi","Camantangan","Concepcion","Dauman","Canidkid","Lebanon","Linoan","Mayaon"};
    String [] Nabunturan = {"Anislagan", "Antequera", "Basak", "Bayabas", "Bukal", "Cabacungan", "Cabidianan", "Katipunan","Matilo", "Mipangi","Ogao","Tagnocon"};
    String [] New_Bataan = {"Andap","Bantacan","Batinao","Cabinuangan(Poblacion)","Camanlang","Cogonon","Fatima","Kahayag","Katipunan","Magangit","Magsaysay","Manurigao"};
    String [] Pantukan = {"Bongabong","Bongbon","P.Fuentes","Kingking(Poblacion)","Magnaga","Matiao","Napnapan","Tagdangua","Tambongon","Tibagon","Las Arenas","Araibo"};
    //davao del sur
    String [] Bansalan = {"Alegre","Alta Vista","Anonang","Bitaug","Bonifacio","Buenavista","Darapuay","Dolo(Urban)","Eman","Kinuskusan","Libertad","Linawan","Mabuhay"};
    String [] Davao_City = {"Poblacion","Talomo","Agdao","Buhangin","Bunawan","Paquibato","Baguio","Calinan","Marilog","Toril","Tugbok"};
    String[] Digos = {"Aplaya", "Balabag", "San Jose (Balutakay)", "Binaton", "Cogon", "Colorado", "Dawis", "Dulangan", "Goma", "Igpit", "Kiagot", "Lungag"};
    String [] Hagonoy = {"Aplaya","Balutakay","Clib","Guihing","Hagonoy Crossing","Kibuaya","La Union","Lanuro","Lapulabao","Leling"};
    String [] Kiblawan = {"Abnate","Balasiao","Bunot","Cogon-Baraca","Ihan","Kibongbong","Kimlawis","Kisulam","Maraga-a","Tacob"};
    String [] Magsaysay = {"Bacunan","Barayong","Dalawinon","Dalumay","Giamang","Kanapulo","Lower Bala","Malawanit","New Ilocos","Tagaytay"};
    String [] Malalag = {"Bagumbayan","Bolton","Caputian","Ibo","Lapla","New Baclayon","Pitu","Tagansule","San Isidro","Baybay","Bulacan","Kiblagon"};
    String [] Matanao = {"Asbang","Bagumbayan","Buas","Camanchiles","Colonsabak","Kabasagan","Kauswagan","La Suerte","Lower Marber","Manga","New Murcia","Saboy"};
    String [] Padada = {"Almendras","Harada Butai","Lower Limonzo","Northern Paligue","Piape","Palili","San Isidro","Southern Paligue","Tulugan","Quirino District","Upper Malinao"};
    String [] Santa_Cruz = {"Astorha","Bato","Coronon","Darong","Inawayan","Jose Rizal","Matutungan","Melilia","Saliducon","Sibulan","Sinoron","Tagabuli"};
    String [] Sulop = {"Balasinon","Carre","Katipunan","Labon","Lapla","Litos","Luparan","New Cebu","Palili","Parame","Roxas","Tagolilong"};
    //Davao del Norte
    String [] Asuncion = {"Binancian","Buclad","Camansa","Cantan","Do??a Andrea","Mapungas","New Santiago","Cambanogoy","San Vicente","Sonlon","New Loon","Buclad"};
    String [] Baraulio_E_Dujali = {"Tuganay","Dujali","Magupising","New Casay","Tanglaw"};
    String [] Carmen = {"Alejal","Anibongang","Asuncion","Guadalupe","La Paz","Mabaus","Magsaysay","Salvacion","Taba","Tibulao","Tubod","Tuganay"};
    String [] Kapalong = {"Semong","Florid","Gabuyan","Gupitan","Capungagan","Katipunan","Luna","Mabantao","Mamacao","Pag-asa","Maniki","Sampao","Sua-on","Tiburcia"};
    String [] New_Corella = {"Cabidianan","Del Monte","El Salvador","Macgum","Mesaoy","New Cortez","Patrocenio","San Roque","Santa Fe","Suawon","Carcor","Mambing"};
    String[] Panabo = {"Buenavista","Cacao","Cagagohan","Consolacion","Dapco","Gredu","Kasilak","Katipunan","Katualan","Kauswagan", "Kiotoy","Mabunao","Maduao","Malativas","Manay","Nanyo", "Salvacion","Santo Ni??o ","Sindaton","Tagpore","Tibungol"};
    String [] Samal = {"Adecor","Aumbay","Balet","Caliclic(Dangca-an)","Catagman","Cogon","Dadatan","Guilon","Kanaan","Libertad","Miranda","Moncadao"};
    String [] San_Isidro = {"Dacudao","Datu Balong","Igangon","Kipalili","Libuton","Linao","Mamangan","Monte Dujali","Pinamuno","Sabangan","San Miguel","Santo Ni??o","Sawata"};
    String[] Santo_Tomas = {"Balagunan","Bobongon","Casig-Ang","Esperanza","Kimamon","Kinamayan","La Libertad","Lungaog","Magwawa","New Katipunan","New Visayas","Pantaron","Salvacion",
            "San Jose","San Miguel","San Vicente","Talomo","Tibal-og","Tulalian"};
    String[] Tagum = {"Apokon", "Bincungan", "Busaon", "Canocotan", "Cuambogan", "La Filipina", "Liboganon", "Madaum", "Magdum", "Magugpo Poblacion", "Mankilam","Pagsabangan", "Pandapan", "Visayan Village"};
    String [] Talaingod = {"Dagohoy","Palma Gil","Santo Ni??o"};
    //Davao Oriental provincial capital
    String [] Baganga = {"Baculin","Batawan","Binondo","Campawan","Dapnan","Lambajon","Mahan-ub","Salingcomot","San Victor","Mikit","Lucod","Kinablangan"};
    String [] Banaybanay = {"Cabangcalan","Caganganan","Calubihan","Causwagan","Punta Linao","Mahayag","Maputi","Mogbongbogon","Panikian","Pintatagan","Piso Proper","San Vicente","Rang-ay"};
    String [] Boston = {"Cabasagan","Caatihan","Cawayanan","Poblacion","San Jose","Sibajay","Carmen","Simulao"};
    String [] Caraga = {"Alvar","Caningag","Lamiawan","Manorigao","Mercedes","Pichon","Poblacion","San Antonio","San Jose","San Luis","San Miguel","San Pedro"};
    String [] Cateel = {"Abihod","Alegria","Aliwagwag","Aragon","Baybay","Maglahus","Mainit","Malibago","Taytayan","San Filomena","San Rafael","San Alfonso"};
    String [] Governor_Generoso = {"Anitap","Lavigan","Magdug","Sigaboy","Pundaguitan","Surop","Tagabebe","Tamban","Tandang Sora","Tibanban","Tiblawan"};
    String [] Lupon = {"Bagumbayan","Cabadiangan","Calapagan","Cocornon","Ilangay","Lantawan","Limbagan","Macangao","Magsaysay","Mahayahay","Maragatas","Marayag","Tagboa"};
    String [] Manay = {"Capasnan","Cawayan","Central","Concepcion","Del Pilar","Guza","Manreza","Rizal","San Fermin","San Ignacio","San Isidro","Taocanga","Zaragosa","Lambog"};
    String [] Mati = {"Badas","Buso", "Central", "Dahican", "Dawan", "Langka",  "Libudon", "Luban",  "Mamali", "Matiao", "Mayo","Sanghay", "Tagabakid", "Tagbinonga", "Taguibo", "Tamisan"};
    String [] San_Isidro_Oriental = {"Baon","Bitaogan","Cambaleon","Dugmanon","Iba","Lapu-lapu","Maag","Manikling","Batobat","Sudlo","Talisay"};
    //Davao Occidental provincial capital
    String [] Don_Marcelino = {"Baluntay","Calinan","Dalupan","Kinanga","Lanao","Lapuan","Lawa","Linadasan","Mabuhay","Talaguton","Nueva Villa"};
    String [] Jose_Abad_Santos = {"Balangonan","Buguis","Bukid","Butan","Caburan","Carahayan","Culaman","Kitayo","Malalan","Marabatuan","Molmol","Sugal","Tabayon","Tanuman"};
    String[] Malita = {"Bito", "Bolia", "Buhangin", "Culaman", "Datu Danwata", "Demloc", "Felis", "Fishing Village", "Kibalatong",  "Tubalan", "Pangaleon"};
    String [] Santa_Maria = {"Basiawan","Cadaatan","Kidadan","Kisulad","Mamacao","Ogpao","Pongpong","San Agustin","Tanglad","Santo Rosaio","San Roque","Datu Taligasao","Datu Intan","Kinilidan"};
    String [] Sarangani = {"Batuganding","Konel","Lipol","Mabila","Tinina","Gomtago","Tagen","Tucal","Patuco","Laker","Camahual","Camalig"};

    //DECLARE VARIABLES
    TextInputLayout fname, lname, localadd, emaill, pass, cmpass, Mobileno;
    Spinner statespin, Cityspin, Suburban;
    Button Signin, Email, Phone;
    CountryCodePicker Cpp;
    ProgressDialog mDialog;

    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String role = "Retailer";
    String statee,cityy,suburban;
    String email,password,firstname,lastname,Localaddress,confirmpass,mobileno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_retailer);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register As Retailer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to main menu
                startActivity(new Intent(RegistrationRetailer.this, MainMenu.class));
                finish();
            }
        });

        try {
            //CONNECT XML
            fname = findViewById(R.id.Fname);
            lname = findViewById(R.id.Lname);
            localadd = findViewById(R.id.Localaddress);
            emaill = findViewById(R.id.emaill);
            pass = findViewById(R.id.Password);
            cmpass = findViewById(R.id.confirmpass);
            Signin = findViewById(R.id.Signup);
            statespin = findViewById(R.id.Statee);
            Cityspin = findViewById(R.id.Citys);
            Suburban = findViewById(R.id.Suburban);
            Mobileno = findViewById(R.id.Mobilenumber);
            Cpp = findViewById(R.id.CountryCode);
            Email = findViewById(R.id.Emailid);
            Phone = findViewById(R.id.phone);

            //PROGRESS DIALOG
            mDialog = new ProgressDialog(RegistrationRetailer.this);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);

            //PROVINCE SPIN
            statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //get spinner data
                    Object value = parent.getItemAtPosition(position);

                    //spinner data - string
                    statee = value.toString().trim();

                    //create array list
                    ArrayList<String> list = new ArrayList<>();

                    //set array to display
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegistrationRetailer.this, android.R.layout.simple_spinner_item, list);

                    //check selected province
                    switch (statee){
                        case "Davao de Oro":
                            Collections.addAll(list, Davao_de_Oro);
                            Cityspin.setAdapter(arrayAdapter);
                            break;
                        case "Davao del Sur":
                            Collections.addAll(list, Davao_del_Sur);
                            Cityspin.setAdapter(arrayAdapter);
                            break;
                        case "Davao del Norte":
                            Collections.addAll(list, Davao_del_Norte);
                            Cityspin.setAdapter(arrayAdapter);
                            break;
                        case "Davao Oriental":
                            Collections.addAll(list, Davao_Oriental);
                            Cityspin.setAdapter(arrayAdapter);
                            break;
                        case "Davao Occidental":
                            Collections.addAll(list, Davao_Occidental);
                            Cityspin.setAdapter(arrayAdapter);
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //CITY SPIN
            Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object value = parent.getItemAtPosition(position);

                    cityy = value.toString().trim();

                    ArrayList<String> list = new ArrayList<>();

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegistrationRetailer.this, android.R.layout.simple_spinner_item, list);

                    //check selected city
                    switch (cityy){
                        //davao de oro
                        case "Compostela":
                            Collections.addAll(list, Compostela);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Laak":
                            Collections.addAll(list, Laak);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Mabini":
                            Collections.addAll(list, Mabini);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Maco":
                            Collections.addAll(list, Maco);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Maragusan":
                            Collections.addAll(list, Maragusan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Mawab":
                            Collections.addAll(list, Mawab);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Monkayo":
                            Collections.addAll(list, Monkayo);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Montivista":
                            Collections.addAll(list, Montivista);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Nabunturan":
                            Collections.addAll(list, Nabunturan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "New Bataan":
                            Collections.addAll(list, New_Bataan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Pantukan":
                            Collections.addAll(list, Pantukan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        //davao del sur
                        case "Bansalan":
                            Collections.addAll(list, Bansalan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Davao City":
                            Collections.addAll(list, Davao_City);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Digos":
                            Collections.addAll(list, Digos);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Hagonoy":
                            Collections.addAll(list, Hagonoy);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Kiblawan":
                            Collections.addAll(list, Kiblawan);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Magsaysay":
                            Collections.addAll(list, Magsaysay);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Malalag":
                            Collections.addAll(list, Malalag);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Matanao":
                            Collections.addAll(list, Matanao);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Padada":
                            Collections.addAll(list, Padada);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Santa Cruz":
                            Collections.addAll(list, Santa_Cruz);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Sulop":
                            Collections.addAll(list, Sulop);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        //davao del norte
                        case "Asuncion":
                            Collections.addAll(list, Asuncion);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Baraulio E. Dujali":
                            Collections.addAll(list, Baraulio_E_Dujali);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Carmen":
                            Collections.addAll(list, Carmen);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Kapalong":
                            Collections.addAll(list, Kapalong);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "New Corella":
                            Collections.addAll(list, New_Corella);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Panabo":
                            Collections.addAll(list, Panabo);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Samal":
                            Collections.addAll(list, Samal);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "San Isidro":
                            Collections.addAll(list, San_Isidro);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Santo Tomas":
                            Collections.addAll(list, Santo_Tomas);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Tagum":
                            Collections.addAll(list, Tagum);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Talaingod":
                            Collections.addAll(list, Talaingod);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        //davao oriental
                        case "Baganga":
                            Collections.addAll(list, Baganga);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Banaybanay":
                            Collections.addAll(list, Banaybanay);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Boston":
                            Collections.addAll(list, Boston);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Caraga":
                            Collections.addAll(list, Caraga);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Cateel":
                            Collections.addAll(list, Cateel);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Governor Generoso":
                            Collections.addAll(list, Governor_Generoso);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Lupon":
                            Collections.addAll(list, Lupon);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Manay":
                            Collections.addAll(list, Manay);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Mati":
                            Collections.addAll(list, Mati);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "San Isidro (Oriental)":
                            Collections.addAll(list, San_Isidro_Oriental);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        //davao occidental
                        case "Don Marcelino":
                            Collections.addAll(list, Don_Marcelino);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Jose Abad Santos":
                            Collections.addAll(list, Jose_Abad_Santos);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Malita":
                            Collections.addAll(list, Malita);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Santa Maria":
                            Collections.addAll(list, Santa_Maria);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                        case "Sarangani":
                            Collections.addAll(list, Sarangani);
                            Suburban.setAdapter(arrayAdapter);
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //BARANGGAY SPIN
            Suburban.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object value = parent.getItemAtPosition(position);

                    suburban = value.toString().trim();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //DATABASE INSTANCES
            firebaseDatabase = FirebaseDatabase.getInstance();
            FAuth = FirebaseAuth.getInstance();

            //BUTTON EVENTS
            //sign up clicked
            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //hide keyboard
                    hideKeyboard();

                    //get string value
                    email = emaill.getEditText().getText().toString().trim();
                    password = pass.getEditText().getText().toString().trim();
                    firstname = fname.getEditText().getText().toString().trim();
                    lastname = lname.getEditText().getText().toString().trim();
                    Localaddress = localadd.getEditText().getText().toString().trim();
                    confirmpass = cmpass.getEditText().getText().toString().trim();
                    mobileno = Mobileno.getEditText().getText().toString().trim();

                    String fullName = firstname+" "+lastname;
                    String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobileno;

                    //check if valid
                    if (isValid()) {
                        mDialog.setMessage("Registration in progress...");
                        mDialog.show();

                        //create user
                        FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check user created
                                if (task.isSuccessful()) {
                                    //get user id
                                    String retailerID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    //user reference
                                    databaseReference = firebaseDatabase.getReference("User").child(retailerID);

                                    //user values
                                    final HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("Role", role);

                                    //set value to reference
                                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //mobile reference
                                            databaseReference = firebaseDatabase.getReference("Mobile").child(phonenumber);

                                            //mobile values
                                            final  HashMap<String, String> phoneMap = new HashMap<>();
                                            phoneMap.put("mobile", phonenumber);
                                            phoneMap.put("id", retailerID);
                                            phoneMap.put("role", role);

                                            //set value to reference
                                            databaseReference.setValue(phoneMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //retailer reference
                                                    databaseReference = firebaseDatabase.getReference("Retailer");

                                                    //retailer values
                                                    HashMap<String, String> retailerMap = new HashMap<>();
                                                    retailerMap.put("Province", statee);
                                                    retailerMap.put("City", cityy);
                                                    retailerMap.put("Baranggay", suburban);
                                                    retailerMap.put("LocalAddress", Localaddress);
                                                    retailerMap.put("EmailID", email);
                                                    retailerMap.put("FirstName", firstname);
                                                    retailerMap.put("LastName", lastname);
                                                    retailerMap.put("FullName", fullName);
                                                    retailerMap.put("Mobile", phonenumber);

                                                    //set value to reference
                                                    databaseReference.child(retailerID).setValue(retailerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            mDialog.dismiss();

                                                            //send email verification
                                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    //check email sent
                                                                    if (task.isSuccessful()) {
                                                                        mDialog.dismiss();
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationRetailer.this);
                                                                        builder.setMessage("Registered Successfully,Please Verify your Email");
                                                                        builder.setCancelable(false);
                                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                                //direct to phone verification + phone number attached
                                                                                startActivity(new Intent(RegistrationRetailer.this, VerifyPhoneRetailer.class).putExtra("phonenumber", phonenumber));
                                                                                finish();
                                                                            }
                                                                        });
                                                                        AlertDialog alert = builder.create();
                                                                        alert.show();
                                                                    } else {
                                                                        mDialog.dismiss();
                                                                        //email not sent
                                                                        ReusableCodeForAll.ShowAlert(RegistrationRetailer.this,"Error",task.getException().getMessage());
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    mDialog.dismiss();
                                    //registration failed
                                    ReusableCodeForAll.ShowAlert(RegistrationRetailer.this,"Error",task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            mDialog.dismiss();
            //try-catch exception
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //email login clicked
        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationRetailer.this, LoginEmailRetailer.class));
                finish();
            }
        });

        //phone login clicked
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationRetailer.this, LoginPhoneRetailer.class));
                finish();
            }
        });
    }

    //EMAIL PATTERN
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //CHECK VALIDITY
    public boolean isValid() {
        emaill.setErrorEnabled(false);
        emaill.setError("");
        fname.setErrorEnabled(false);
        fname.setError("");
        lname.setErrorEnabled(false);
        lname.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");
        cmpass.setErrorEnabled(false);
        cmpass.setError("");
        Mobileno.setErrorEnabled(false);
        Mobileno.setError("");

        boolean isValidfirstname = false, isValidlastname = false, isValidaddress = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false;
        if (TextUtils.isEmpty(firstname)) {
            fname.setErrorEnabled(true);
            fname.setError("FirstName is required");
        } else {
            isValidfirstname = true;
        }
        if (TextUtils.isEmpty(lastname)) {
            lname.setErrorEnabled(true);
            lname.setError("LastName is required");
        } else {
            isValidlastname = true;
        }
        if (TextUtils.isEmpty(email)) {
            emaill.setErrorEnabled(true);
            emaill.setError("Email is required");
        } else {
            if (email.matches(emailpattern)) {
                isValidemail = true;
            } else {
                emaill.setErrorEnabled(true);
                emaill.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(mobileno)) {
            Mobileno.setErrorEnabled(true);
            Mobileno.setError("Mobile number is required");
        } else {
            if (mobileno.length() < 10) {
                Mobileno.setErrorEnabled(true);
                Mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(password)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            if (password.length() < 6) {
                pass.setErrorEnabled(true);
                pass.setError("Password too weak");
                cmpass.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpass)) {
            cmpass.setErrorEnabled(true);
            cmpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(confirmpass)) {
                pass.setErrorEnabled(true);
                pass.setError("Password doesn't match");
                cmpass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(Localaddress)) {
            localadd.setErrorEnabled(true);
            localadd.setError("Local Address is required");
        } else {
            isValidaddress = true;
        }
        isvalid = isValidfirstname && isValidlastname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno && isValidaddress;
        return isvalid; //return true/false
    }

    //HIDE KEYBOARD
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager hide = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            hide.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //DISABLE BACK PRESS
    public void onBackPressed(){}
}