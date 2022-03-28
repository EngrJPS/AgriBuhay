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
import java.util.HashMap;

public class Delivery_registeration extends AppCompatActivity {

    //TODO change the string names to the barangay names
    //This strings will be the province of Davao
    String [] Davao_de_Oro = {"Compostela", "Laak", "Mabini", "Maco", "Maragusan", "Mawab"
            , "Monkayo", "Montivista", "Nabunturan", "New Bataan", "Pantukan"};

    String [] Davao_del_Sur = {"Bansalan", "Davao City", "Digos", "Hagonoy", "Kiblawan"
            , "Magsaysay", "Malalag", "Matanao", "Padada", "Santa Cruz", "Sulop"};

    String [] Davao_del_Norte = {"Asuncion", "Baraulio E. Dujali", "Carmen", "Kapalong", "New Corella"
            , "Panabo", "Samal", "San Isidro", "Tagum", "Talaingud"};

    String [] Davao_Oriental = {"Baganga", "Banaybanay", "Boston", "Caraga", "Cateel"
            , "Governor Generoso", "Lupon", "Manay", "Mati", "San Isidro", "Mati"};

    String [] Davao_Occidental = {"Don Marcelino", "Jose Abad Santos", "Malita", "Santa Maria", "Sarangani"};

    //Davao De Oro Provincial Capital
    String [] Nabunturan = {"Anislagan", "Antequera", "Basak", "Bayabas", "Bukal", "Cabacungan", "Cabidianan", "Katipunan"
            , "Libasan", "Linda", "Magading", "Magsaysay", "Mainit", "Manat", "Matilo", "Mipangi", "New Dauis", "New Sibonga", "Ogao"
            , "Pangutosan", "San Isidro", "San Roque", "San Vicente", "Santa Maria", "Santo Niño (Kao)", "Sasa", "Tagnocon"};

    //Research scope area
    String [] Maco = {"Anibongan", "Anislagan", "Binuangan", "Bucana", "Calabcab", "Concepcion", "Dumlan", "Elizalde (Somil)", "Pangi (Gaudencio Antonio)"
            , "Gubatan", "Hijo", "Kinuban", "Langgam", "Lapu-lapu", "Libay-libay", "Limbo", "Lumatab", "Magangit", "Malamodao", "Manipongol"
            , "Mapaang", "Masara", "New Asturias", "Panibasan", "Panoraon", "San Juan", "San Roque", "Sangab", "Taglawig"
            , "Mainit", "New Barili", "New Leyte", "New Visayas", "Panangan", "Tagbaros", "Teresa"};

    //Davao del Sur provincial capital
    String[] Digos = {"Aplaya", "Balabag", "San Jose (Balutakay)", "Binaton", "Cogon", "Colorado", "Dawis", "Dulangan", "Goma", "Igpit", "Kiagot", "Lungag", "Mahayahay"
            , "Matti", "Kapatagan (Rizal)", "Ruparan", "San Agustin", "San Miguel (Odaca)", "San Roque", "Sinawilan", "Soong", "Tiguman", "Tres De Mayo"};

    //Davao del Norte Provicial capital
    String[] Tagum = {"Apokon", "Bincungan", "Busaon", "Canocotan", "Cuambogan", "La Filipina", "Liboganon", "Madaum", "Magdum", "Magugpo Poblacion", "Mankilam"
            , "New Balamban", "Nueva Fuerza", "Pagsabangan", "Pandapan", "San Agustin", "San Isidro", "San Miguel (Camp 4)", "Visayan Village"};

    //Davao Oriental provincial capital
    String[] Mati = {"Badas", "Bobon", "Buso", "Cabuaya", "Central (City Proper/Poblacion)", "Culian"
            , "Dahican", "Danao", "Dawan", "Don Enrique Lopez", "Don Martin Marundan", "Don Salvador Lopez, Sr."
            , "Langka", "Lawigan", "Libudon", "Luban", "Macambol", "Mamali", "Matiao", "Mayo"
            , "Sainz", "Sanghay", "Tagabakid", "Tagbinonga", "Taguibo", "Tamisan"};
    //Davao Occidental provincial capital
    String[] Malita = {"Bito", "Bolia", "Buhangin", "Culaman", "Datu Danwata", "Demloc", "Felis", "Fishing Village"
            , "Kibalatong", "Kidalapong", "Kilalag", "Kinangan", "Lacaron", "Lagumit", "Lais", "Little Baguio", "Macol"
            , "Mana", "Manuel Peralta", "New Argao", "Pangian", "Pinalpalan", "Poblacion", "Sangay", "Talogoy", "Tical"
            , "Ticulon", "Tingolo", "Tubalan", "Pangaleon"};


    TextInputLayout Fname, Lname, Pass, cfpass, mobileno, houseno, area, postcode, Email;
    Spinner statespin, Cityspin, Suburban;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth FAuth;
    String role = "DeliveryPerson";
    String statee, cityy, suburban, fname, lname, mobile, confirmpassword, password, Area, Postcode, house, emailid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registeration);

        Fname = (TextInputLayout) findViewById(R.id.fname);
        Lname = (TextInputLayout) findViewById(R.id.lname);
        Pass = (TextInputLayout) findViewById(R.id.password);
        Email = (TextInputLayout) findViewById(R.id.Emailid);
        cfpass = (TextInputLayout) findViewById(R.id.confirmpassword);
        mobileno = (TextInputLayout) findViewById(R.id.mobileno);
        houseno = (TextInputLayout) findViewById(R.id.Houseno);
        area = (TextInputLayout) findViewById(R.id.Areaa);
        postcode = (TextInputLayout) findViewById(R.id.Postcodee);
        statespin = (Spinner) findViewById(R.id.State);
        Cityspin = (Spinner) findViewById(R.id.City);
        Emaill = (Button) findViewById(R.id.emaillid);
        Suburban = (Spinner) findViewById(R.id.suburban);
        signup = (Button) findViewById(R.id.Signupp);
        Phone = (Button) findViewById(R.id.Phonenumber);
        Cpp = (CountryCodePicker) findViewById(R.id.ctrycode);
        final ProgressDialog mDialog = new ProgressDialog(Delivery_registeration.this);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register As Logistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO change the statespin names to the maco barangay names
        statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if (statee.equals("Davao de Oro")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Davao_de_Oro) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

                if (statee.equals("Davao del Sur")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Davao_del_Sur) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

                if (statee.equals("Davao del Norte")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Davao_del_Norte) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

                if (statee.equals("Davao Oriental")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Davao_Oriental) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

                if (statee.equals("Davao Occidental")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Davao_Occidental) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Cityspin.setAdapter(arrayAdapter);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();
                //Davao del Norte provinces
                if (cityy.equals("Maco")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Maco) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Nabunturan")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Nabunturan) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Digos")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Digos) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Tagum")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Tagum) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Mati")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Mati) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }

                if (cityy.equals("Malita")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String text : Malita) {
                        list.add(text);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_registeration.this, android.R.layout.simple_spinner_item, list);

                    Suburban.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


        //TODO change the reference name to Logistics
        databaseReference = firebaseDatabase.getInstance().getReference("DeliveryPerson");
        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confirmpassword = cfpass.getEditText().getText().toString().trim();
                Area = area.getEditText().getText().toString().trim();
                house = houseno.getEditText().getText().toString().trim();
                Postcode = postcode.getEditText().getText().toString().trim();

                if (isValid()) {


                    mDialog.setMessage("Registering please wait...");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                //TODO this is the database for the DeliveryPerson
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
                                        //Todo change the deliveryperson to Logistics
                                        firebaseDatabase.getInstance().getReference("DeliverPerson").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();

                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Delivery_registeration.this);
                                                            builder.setMessage("Registered Successfully,Please Verify your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    dialog.dismiss();

                                                                    String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                                    Intent b = new Intent(Delivery_registeration.this, Delivery_VerifyPhone.class);
                                                                    b.putExtra("phonenumber", phonenumber);
                                                                    startActivity(b);

                                                                }
                                                            });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();

                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(Delivery_registeration.this, "Error", task.getException().getMessage());

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(Delivery_registeration.this, "Error", task.getException().getMessage());
                            }

                        }
                    });


                }
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent e = new Intent(Delivery_registeration.this, Delivery_LoginPhone.class);
                startActivity(e);
                finish();
            }
        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Delivery_registeration.this, Delivery_Login.class);
                startActivity(a);
                finish();
            }
        });


    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid() {
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Email.setErrorEnabled(false);
        Email.setError("");
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

        boolean isValidname = false, isvalidpassword = false, isValidemail = false, isvalidconfirmpassword = false, isvalid = false, isvalidmobileno = false, isvalidlname = false, isvalidhousestreetno = false, isvalidarea = false, isvalidpostcode = false;
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
        } else {
            isvalidpostcode = true;
        }

        isvalid = (isValidname && isvalidpostcode && isValidemail && isvalidlname && isvalidconfirmpassword && isvalidpassword && isvalidmobileno && isvalidarea && isvalidhousestreetno) ? true : false;
        return isvalid;
    }
}
