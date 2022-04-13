package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

public class RegistrationProducer extends AppCompatActivity {

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
    String [] Compostela = {"Bagongon", "Gabi", "Lagab", "Mangayon", "Mapaca", "Maparat", "New Alegria", "Ngan", "Osmeña", "Panansalan", "Poblacion", "San Jose"};
    String [] Laak = {"Aguinaldo","Ampawid","Anitap","Banbanon","Binasbas","Ceboleda","Datu Ampunan", "Doña Josefa","Il Papa","Inakayan","Kapatagan","Kidawa","Kiokmay"};
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
    String [] Asuncion = {"Binancian","Buclad","Camansa","Cantan","Doña Andrea","Mapungas","New Santiago","Cambanogoy","San Vicente","Sonlon","New Loon","Buclad"};
    String [] Baraulio_E_Dujali = {"Tuganay","Dujali","Magupising","New Casay","Tanglaw"};
    String [] Carmen = {"Alejal","Anibongang","Asuncion","Guadalupe","La Paz","Mabaus","Magsaysay","Salvacion","Taba","Tibulao","Tubod","Tuganay"};
    String [] Kapalong = {"Semong","Florid","Gabuyan","Gupitan","Capungagan","Katipunan","Luna","Mabantao","Mamacao","Pag-asa","Maniki","Sampao","Sua-on","Tiburcia"};
    String [] New_Corella = {"Cabidianan","Del Monte","El Salvador","Macgum","Mesaoy","New Cortez","Patrocenio","San Roque","Santa Fe","Suawon","Carcor","Mambing"};
    String[] Panabo = {"Buenavista","Cacao","Cagagohan","Consolacion","Dapco","Gredu","Kasilak","Katipunan","Katualan","Kauswagan", "Kiotoy","Mabunao","Maduao","Malativas","Manay","Nanyo", "Salvacion","Santo Niño ","Sindaton","Tagpore","Tibungol"};
    String [] Samal = {"Adecor","Aumbay","Balet","Caliclic(Dangca-an)","Catagman","Cogon","Dadatan","Guilon","Kanaan","Libertad","Miranda","Moncadao"};
    String [] San_Isidro = {"Dacudao","Datu Balong","Igangon","Kipalili","Libuton","Linao","Mamangan","Monte Dujali","Pinamuno","Sabangan","San Miguel","Santo Niño","Sawata"};
    String[] Santo_Tomas = {"Balagunan","Bobongon","Casig-Ang","Esperanza","Kimamon","Kinamayan","La Libertad","Lungaog","Magwawa","New Katipunan","New Visayas","Pantaron","Salvacion",
            "San Jose","San Miguel","San Vicente","Talomo","Tibal-og","Tulalian"};
    String[] Tagum = {"Apokon", "Bincungan", "Busaon", "Canocotan", "Cuambogan", "La Filipina", "Liboganon", "Madaum", "Magdum", "Magugpo Poblacion", "Mankilam","Pagsabangan", "Pandapan", "Visayan Village"};
    String [] Talaingod = {"Dagohoy","Palma Gil","Santo Niño"};
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

    TextInputLayout Fname, Lname, Email, Pass, cfpass, mobileno, houseno, area, postcode;
    Spinner statespin, Cityspin, Suburban;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname;
    String lname;
    String emailid;
    String password;
    String confirmpassword;
    String mobile;
    String house;
    String Area;
    String Postcode;
    String role = "Chef";
    String statee;
    String cityy;
    String suburban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_producer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register As Producer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Fname = (TextInputLayout) findViewById(R.id.Firstname);
        Lname = (TextInputLayout) findViewById(R.id.Lastname);
        Email = (TextInputLayout) findViewById(R.id.Email);
        Pass = (TextInputLayout) findViewById(R.id.Pwd);
        cfpass = (TextInputLayout) findViewById(R.id.Cpass);
        mobileno = (TextInputLayout) findViewById(R.id.Mobileno);
        houseno = (TextInputLayout) findViewById(R.id.houseNo);
        area = (TextInputLayout) findViewById(R.id.Area);
        postcode = (TextInputLayout) findViewById(R.id.Postcode);
        statespin = (Spinner) findViewById(R.id.Statee);
        Cityspin = (Spinner) findViewById(R.id.Citys);
        Suburban = (Spinner) findViewById(R.id.Suburban);
        signup = (Button) findViewById(R.id.Signup);
        Emaill = (Button) findViewById(R.id.emaill);
        Phone = (Button) findViewById(R.id.phone);
        Cpp = (CountryCodePicker) findViewById(R.id.CountryCode);

        //PROVINCE SPIN
        statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                ArrayList<String> list = new ArrayList<>();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegistrationProducer.this, android.R.layout.simple_spinner_item, list);
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegistrationProducer.this, android.R.layout.simple_spinner_item, list);
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

        //This line will create a database to the firebase
        databaseReference = firebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confirmpassword = cfpass.getEditText().getText().toString().trim();
                Area = area.getEditText().getText().toString().trim();
                house = houseno.getEditText().getText().toString().trim();
                Postcode = postcode.getEditText().getText().toString().trim();
                String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;


                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(RegistrationProducer.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registering please wait...");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                //TODO this is the database for the chef
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String, String> hashMappp = new HashMap<>();
                                        hashMappp.put("Area", Area);
                                        hashMappp.put("City", cityy);
                                        hashMappp.put("ConfirmPassword", confirmpassword);
                                        hashMappp.put("EmailID", emailid);
                                        hashMappp.put("Fname", fname);
                                        hashMappp.put("House", house);
                                        hashMappp.put("Lname", lname);
                                        hashMappp.put("Mobile", mobile);
                                        hashMappp.put("Password", password);
                                        hashMappp.put("Postcode", Postcode);
                                        hashMappp.put("State", statee);
                                        hashMappp.put("Suburban", suburban);
                                        firebaseDatabase.getInstance().getReference("Chef")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();

                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationProducer.this);
                                                            builder.setMessage("Registered Successfully,Please Verify your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    dialog.dismiss();


                                                                    Intent b = new Intent(RegistrationProducer.this, ChefVerifyPhone.class);
                                                                    b.putExtra("phonenumber", phonenumber);
                                                                    startActivity(b);

                                                                }
                                                            });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(RegistrationProducer.this, "Error", task.getException().getMessage());

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(RegistrationProducer.this, "Error", task.getException().getMessage());
                            }

                        }
                    });

                }

            }

        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegistrationProducer.this, ChefLogin.class);
                startActivity(i);
                finish();
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent e = new Intent(RegistrationProducer.this, Chefloginphone.class);
                startActivity(e);
                finish();
            }
        });


    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cfpass.setErrorEnabled(false);
        cfpass.setError("");
        area.setErrorEnabled(false);
        area.setError("");
        houseno.setErrorEnabled(false);
        houseno.setError("");
        postcode.setErrorEnabled(false);
        postcode.setError("");

        boolean isValidname = false, isValidemail = false, isvalidpassword = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false, isvalidhousestreetno = false, isvalidarea = false, isvalidpostcode = false;
        if (TextUtils.isEmpty(fname)) {
            Fname.setErrorEnabled(true);
            Fname.setError("Firstname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(lname)) {
            Lname.setErrorEnabled(true);
            Lname.setError("Lastname is required");
        } else {
            isvalidlname = true;
        }
        if (TextUtils.isEmpty(emailid)) {
            Email.setErrorEnabled(true);
            Email.setError("Email is required");
        } else {
            if (emailid.matches(emailpattern)) {
                isValidemail = true;
            } else {
                Email.setErrorEnabled(true);
                Email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password)) {
            Pass.setErrorEnabled(true);
            Pass.setError("Password is required");
        } else {
            if (password.length() < 6) {
                Pass.setErrorEnabled(true);
                Pass.setError("password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            cfpass.setErrorEnabled(true);
            cfpass.setError("Confirm Password is required");
        } else {
            if (!password.equals(confirmpassword)) {
                Pass.setErrorEnabled(true);
                Pass.setError("Password doesn't match");
            } else {
                isvalidconfirmpassword = true;
            }
        }
        if (TextUtils.isEmpty(mobile)) {
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile number is required");
        } else {
            if (mobile.length() < 10) {
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(house)) {
            houseno.setErrorEnabled(true);
            houseno.setError("Field cannot be empty");
        } else {
            isvalidhousestreetno = true;
        }
        if (TextUtils.isEmpty(Area)) {
            area.setErrorEnabled(true);
            area.setError("Field cannot be empty");
        } else {
            isvalidarea = true;
        }
        if (TextUtils.isEmpty(Postcode)) {
            postcode.setErrorEnabled(true);
            postcode.setError("Field cannot be empty");
        } else if(Postcode.length()<4){
            postcode.setErrorEnabled(true);
            postcode.setError("Invalid Code");
        } else {
            isvalidpostcode = true;
        }

        isvalid = (isValidname && isvalidpostcode && isvalidlname && isValidemail && isvalidconfirmpassword && isvalidpassword && isvalidmobileno && isvalidarea && isvalidhousestreetno) ? true : false;
        return isvalid;
    }
}