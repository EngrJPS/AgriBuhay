package com.AgriBuhayProj.app.RetailerPanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AgriBuhayProj.app.Models.Retailer;
import com.AgriBuhayProj.app.MainMenu;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//PROFILE FRAGMENT
public class RetailerProfileFragment extends Fragment {

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

    //VARIABLES
    EditText firstname, lastname, address;
    Spinner Province, City, Baranggay;
    TextView mobileno, Email;
    Button Update;
    LinearLayout password, LogOut;

    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    String province, city, baranggay, email, passwordd, confirmpass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        View v = inflater.inflate(R.layout.fragment_retailerprofile, null);
        //CONNECT XML
        firstname = v.findViewById(R.id.fnamee);
        lastname = v.findViewById(R.id.lnamee);
        address = v.findViewById(R.id.address);
        Email = v.findViewById(R.id.emailID);
        Province = v.findViewById(R.id.rState);
        City = v.findViewById(R.id.rCity);
        Baranggay = v.findViewById(R.id.rBar);
        mobileno = v.findViewById(R.id.mobilenumber);
        Update = v.findViewById(R.id.update);
        password = v.findViewById(R.id.passwordlayout);
        LogOut = v.findViewById(R.id.logout_layout);

        //AUTHENTICATION
        firebaseAuth = FirebaseAuth.getInstance();
        String userid = firebaseAuth.getCurrentUser().getUid();

        //GET RETAILER
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //GET RETAILER PHONE NUMBER
        String mobile = user.getPhoneNumber();

        //RETAILER REFERENCE
        databaseReference = FirebaseDatabase.getInstance().getReference("Retailer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Retailer retailer = dataSnapshot.getValue(Retailer.class);

                //set values
                firstname.setText(retailer.getFirstName());
                lastname.setText(retailer.getLastName());
                address.setText(retailer.getLocalAddress());
                mobileno.setText(mobile);
                Email.setText(retailer.getEmailID());

                //set spinner values
                Province.setSelection(getIndexByString(Province, retailer.getProvince()));
                City.setSelection(getIndexByString(City, retailer.getCity()));
                Baranggay.setSelection(getIndexByString(Baranggay, retailer.getBaranggay()));

                //province selected
                Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        province = value.toString().trim();
                        ArrayList<String> list = new ArrayList<>();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);

                        //display city
                        switch (province){
                            case "Davao de Oro":
                                Collections.addAll(list, Davao_de_Oro);
                                City.setAdapter(arrayAdapter);
                                break;
                            case "Davao del Sur":
                                Collections.addAll(list, Davao_del_Sur);
                                City.setAdapter(arrayAdapter);
                                break;
                            case "Davao del Norte":
                                Collections.addAll(list, Davao_del_Norte);
                                City.setAdapter(arrayAdapter);
                                break;
                            case "Davao Oriental":
                                Collections.addAll(list, Davao_Oriental);
                                City.setAdapter(arrayAdapter);
                                break;
                            case "Davao Occidental":
                                Collections.addAll(list, Davao_Occidental);
                                City.setAdapter(arrayAdapter);
                                break;
                        }

                        City.setSelection(getIndexByString(City, retailer.getCity()));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //city selected
                City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        city = value.toString().trim();
                        ArrayList<String> list = new ArrayList<>();
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);

                        //display baranggay
                        switch (city){
                            //davao de oro
                            case "Compostela":
                                Collections.addAll(list, Compostela);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Laak":
                                Collections.addAll(list, Laak);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Mabini":
                                Collections.addAll(list, Mabini);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Maco":
                                Collections.addAll(list, Maco);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Maragusan":
                                Collections.addAll(list, Maragusan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Mawab":
                                Collections.addAll(list, Mawab);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Monkayo":
                                Collections.addAll(list, Monkayo);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Montivista":
                                Collections.addAll(list, Montivista);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Nabunturan":
                                Collections.addAll(list, Nabunturan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "New Bataan":
                                Collections.addAll(list, New_Bataan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Pantukan":
                                Collections.addAll(list, Pantukan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            //davao del sur
                            case "Bansalan":
                                Collections.addAll(list, Bansalan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Davao City":
                                Collections.addAll(list, Davao_City);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Digos":
                                Collections.addAll(list, Digos);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Hagonoy":
                                Collections.addAll(list, Hagonoy);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Kiblawan":
                                Collections.addAll(list, Kiblawan);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Magsaysay":
                                Collections.addAll(list, Magsaysay);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Malalag":
                                Collections.addAll(list, Malalag);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Matanao":
                                Collections.addAll(list, Matanao);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Padada":
                                Collections.addAll(list, Padada);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Santa Cruz":
                                Collections.addAll(list, Santa_Cruz);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Sulop":
                                Collections.addAll(list, Sulop);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            //davao del norte
                            case "Asuncion":
                                Collections.addAll(list, Asuncion);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Baraulio E. Dujali":
                                Collections.addAll(list, Baraulio_E_Dujali);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Carmen":
                                Collections.addAll(list, Carmen);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Kapalong":
                                Collections.addAll(list, Kapalong);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "New Corella":
                                Collections.addAll(list, New_Corella);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Panabo":
                                Collections.addAll(list, Panabo);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Samal":
                                Collections.addAll(list, Samal);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "San Isidro":
                                Collections.addAll(list, San_Isidro);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Santo Tomas":
                                Collections.addAll(list, Santo_Tomas);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Tagum":
                                Collections.addAll(list, Tagum);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Talaingod":
                                Collections.addAll(list, Talaingod);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            //davao oriental
                            case "Baganga":
                                Collections.addAll(list, Baganga);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Banaybanay":
                                Collections.addAll(list, Banaybanay);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Boston":
                                Collections.addAll(list, Boston);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Caraga":
                                Collections.addAll(list, Caraga);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Cateel":
                                Collections.addAll(list, Cateel);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Governor Generoso":
                                Collections.addAll(list, Governor_Generoso);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Lupon":
                                Collections.addAll(list, Lupon);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Manay":
                                Collections.addAll(list, Manay);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Mati":
                                Collections.addAll(list, Mati);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "San Isidro (Oriental)":
                                Collections.addAll(list, San_Isidro_Oriental);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            //davao occidental
                            case "Don Marcelino":
                                Collections.addAll(list, Don_Marcelino);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Jose Abad Santos":
                                Collections.addAll(list, Jose_Abad_Santos);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Malita":
                                Collections.addAll(list, Malita);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Santa Maria":
                                Collections.addAll(list, Santa_Maria);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                            case "Sarangani":
                                Collections.addAll(list, Sarangani);
                                Baranggay.setAdapter(arrayAdapter);
                                break;
                        }
                        
                        Baranggay.setSelection(getIndexByString(Baranggay, retailer.getBaranggay()));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //baranggay selected
                Baranggay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object value = parent.getItemAtPosition(position);
                        baranggay = value.toString().trim();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //update information
        updateinformation();
        return v;
    }
    //TODO INCOMPLETE
    private void updateinformation() {
        //update information
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get retailer id
                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                data = FirebaseDatabase.getInstance().getReference("Retailer").child(useridd);
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Retailer retailer = dataSnapshot.getValue(Retailer.class);

                        //get values
                        email = retailer.getEmailID();
                        long mobilenoo = Long.parseLong(retailer.getMobile());

                        String Fname = firstname.getText().toString().trim();
                        String Lname = lastname.getText().toString().trim();
                        String fullName = Fname+" "+Lname;
                        String Address = address.getText().toString().trim();

                        //set new values
                        HashMap<String, String> hashMappp = new HashMap<>();
                        hashMappp.put("EmailID", email);
                        hashMappp.put("FirstName", Fname);
                        hashMappp.put("LastName", Lname);
                        hashMappp.put("FullName", fullName);
                        hashMappp.put("Mobile", mobileno.getText().toString());;
                        hashMappp.put("LocalAddress", Address);
                        hashMappp.put("Province", province);
                        hashMappp.put("City", city);
                        hashMappp.put("Baranggay", baranggay);

                        //add to retailer db
                        firebaseDatabase.getInstance().getReference("Retailer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

        //reset password
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RetailerForgotPassword.class));
            }
        });

        //change mobile no
        mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RetailerPhoneNumber.class));
            }
        });

        //logout user
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //logout user
                        firebaseAuth.signOut();
                        //direct to main menu
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    //GET ARRAY INDEX BY STRING
    private int getIndexByString(Spinner st, String spist) {
        int index = 0;
        for (int i = 0; i < st.getCount(); i++) {
            if (st.getItemAtPosition(i).toString().equalsIgnoreCase(spist)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
