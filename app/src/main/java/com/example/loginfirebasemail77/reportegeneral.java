package com.example.loginfirebasemail77;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class reportegeneral extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList alertas ;
    PieChart pieChart;
    int cantUbicaciones=0, cantSensores=0, cantReconocimiento=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporte_general);
        pieChart= findViewById(R.id.graficaC);

        alertas=new ArrayList();

        inicializarFirebase();
        obtenerUbicaciones();


    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    private void obtenerUbicaciones() {
        databaseReference.child("ubicaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertas.add(snapshot.getChildrenCount());
                informacionSensores(alertas);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void informacionSensores(ArrayList alertas) {
        databaseReference.child("Sensores").orderByKey().equalTo("2C:F4:32:19:78:F6").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertas.add(snapshot.getChildrenCount());
                obtenerReconocimiento(alertas);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void obtenerReconocimiento(ArrayList alertas) {
        databaseReference.child("reconocimiento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alertas.add(snapshot.getChildrenCount());
                generalGrafica(alertas);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void generalGrafica(ArrayList alertas)
    {
        cantUbicaciones=Integer.parseInt(alertas.get(0).toString());
        cantSensores=Integer.parseInt(alertas.get(1).toString());
        cantReconocimiento=Integer.parseInt(alertas.get(2).toString());

        ArrayList<PieEntry> visitors= new ArrayList<>();
        visitors.add(new PieEntry(cantUbicaciones,"Ubicaciones"));
        visitors.add(new PieEntry(cantSensores,"Ultrasonicas"));
        visitors.add(new PieEntry(cantReconocimiento,"Reconocimiento"));
        PieDataSet pieDataSet=new PieDataSet(visitors, "Visitors");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setFormSize(20);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16f);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Cantidades totales");
        pieChart.animate();

    }
}