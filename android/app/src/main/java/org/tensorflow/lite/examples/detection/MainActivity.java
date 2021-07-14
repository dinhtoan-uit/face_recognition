package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.KMeansOnline.Centroid;
import org.tensorflow.lite.examples.detection.MTCNN.BorderedText;
import org.tensorflow.lite.examples.detection.MTCNN.Box;
import org.tensorflow.lite.examples.detection.MTCNN.MTCNN;
import org.tensorflow.lite.examples.detection.MTCNN.Util;
import org.tensorflow.lite.examples.detection.FaceNet.FaceNet;
import org.tensorflow.lite.examples.detection.KMeansOnline.KMeansOnline;
import org.tensorflow.lite.examples.detection.adapter.ItemAdapter;
import org.tensorflow.lite.examples.detection.model.Item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private MTCNN detect;
    private FaceNet mfn;
    private Context context;
    private KMeansOnline kmeans = new KMeansOnline();
    private BorderedText borderedText;
    private Canvas canvas;
    public static Bitmap imageBitmap;
    public static Bitmap bitmapCrop;

    public float[][] embedding = new float[1][192];
    public String text;
    ArrayList<Centroid> centroids = new ArrayList<>();

    public static final int PERMISSION_REQUESS = 15;
    public static final int MY_AVATAR_KEY = 1975;
    File ahihi = new File();
    public static final int REQUEST_IMAGE_CAPTURE = 1969;
    public static final int PERMISSION_CAPTURE = 2001;
    public static final int RESULT_LOAD_IMAGE = 2000;

    RecyclerView recyclerView;
    List<Item> itemList;
    ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            detect = new MTCNN(getAssets());
            mfn = new FaceNet(getAssets());
            centroids = getCentroids();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerItem);
        itemList = new ArrayList<>();
        createItemList();
        itemAdapter = new ItemAdapter(this,itemList);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createItemList()
    {
        Bitmap bitmap;

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.kristanna_koken);
        itemList.add(new Item("Label 1 \n Kristanna Koken",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.kurt_warner);
        itemList.add(new Item("Label 2 \n Kurt Warner",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lance_bass);
        itemList.add(new Item("Label 3 \n Lance Bass",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.larry_coker);
        itemList.add(new Item("Label 4 \n Larry Coker",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.larry_thompson);
        itemList.add(new Item("Label 5 \n Larry Thompson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.laura_linney);
        itemList.add(new Item("Label 6 \n Laura Linney",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lebron_james);
        itemList.add(new Item("Label 7 \n Lebron James",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lee_hoi_chang);
        itemList.add(new Item("Label 8 \n Lee Hoi Chang",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lisa_marie_presley);
        itemList.add(new Item("Label 9 \n Lisa Marie Presley",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lucy_liu);
        itemList.add(new Item("Label 10 \n Lucy Liu",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ludivine_sagnier);
        itemList.add(new Item("Label 11 \n Ludivine Sagnier",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.luis_figo);
        itemList.add(new Item("Label 12 \n Luis_Figo",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.luis_gonzalez_macchi);
        itemList.add(new Item("Label 13 \n Luis Gonzalez Macchi",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.luis_horna);
        itemList.add(new Item("Label 14 \n Luis Horna",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.madonna);
        itemList.add(new Item("Label 15 \n Madonna",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.marc_grossman);
        itemList.add(new Item("Label 16 \n Marc Grossman",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.maria_soledad_alvear_valenzuela);
        itemList.add(new Item("Label 17 \n Maria Soledad Alvear Valenzuela",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mark_hurlbert);
        itemList.add(new Item("Label 18 \n Mark Hurlbert",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mark_wahlberg);
        itemList.add(new Item("Label 19 \n Mark Wahlberg",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.martha_lucia_ramirez);
        itemList.add(new Item("Label 20 \n Martha Lucia Ramirez",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.martha_stewart);
        itemList.add(new Item("Label 21 \n Martha Stewart",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.martina_mcbride);
        itemList.add(new Item("Label 22 \n Martina Mcbride",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.martin_mcguinness);
        itemList.add(new Item("Label 23 \n Martin McGuinness",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.matt_damon);
        itemList.add(new Item("Label 24 \n Matt Damon",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.miankhursheedmehmoodkasuri);
        itemList.add(new Item("Label 25 \n Mian Khursheed Mehmood Kasuri",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michael_chiklis);
        itemList.add(new Item("Label 26 \n Michael Chiklis",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michael_douglas);
        itemList.add(new Item("Label 27 \n Michael Douglas",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michael_jordan);
        itemList.add(new Item("Label 28 \n Michael Jordan",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michael_phelps);
        itemList.add(new Item("Label 29 \n Michael Phelps",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michael_powell);
        itemList.add(new Item("Label 30 \n Michael Powell",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.michelle_yeoh);
        itemList.add(new Item("Label 31 \n Michelle Yeoh",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mick_jagger);
        itemList.add(new Item("Label 32 \n Mick Jagger",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mike_krzyzewski);
        itemList.add(new Item("Label 33 \n Mike Krzyzewski",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mike_tyson);
        itemList.add(new Item("Label 34 \n Mike Tyson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mikhail_kasyanov);
        itemList.add(new Item("Label 35 \n Mikhail Kasyanov",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mikhail_wehbe);
        itemList.add(new Item("Label 36 \n Mikhail Wehbe",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mireya_moscoso);
        itemList.add(new Item("Label 37 \n Mireya Moscoso",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mitchell_daniels);
        itemList.add(new Item("Label 38 \n Mitchell Daniels",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.monica_bellucci);
        itemList.add(new Item("Label 39 \n Monica Bellucci",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.monica_seles);
        itemList.add(new Item("Label 40 \n Monica Seles",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.moshe_katsav);
        itemList.add(new Item("Label 41 \n Moshe Katsav",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.muhammad_saeed_al_sahhaf);
        itemList.add(new Item("Label 42 \n Muhammad Saeed Al-Sahhaf",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nadia_petrova);
        itemList.add(new Item("Label 43 \n Nadia Petrova",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nan_wang);
        itemList.add(new Item("Label 44 \n Nan Wang",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.naoto_kan);
        itemList.add(new Item("Label 46 \n Naoto Kan",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.natalie_coughlin);
        itemList.add(new Item("Label 47 \n Natalie Coughlin",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nathalie_baye);
        itemList.add(new Item("Label 48 \n Natalie Baye",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nelson_mandela);
        itemList.add(new Item("Label 49 \n Nelson Mandela",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nia_vardalos);
        itemList.add(new Item("Label 50 \n Nia Vardalos",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nick_nolte);
        itemList.add(new Item("Label 51 \n Nick Nolte",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.nicolas_cage);
        itemList.add(new Item("Label 52 \n Nicolas Cage",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.noelle_bush);
        itemList.add(new Item("Label 53 \n Noelle Bush",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.omar_sharif);
        itemList.add(new Item("Label 54 \n Omar Sharif",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.oprah_winfrey);
        itemList.add(new Item("Label 55 \n Oprah Winfrey",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.osama_bin_laden);
        itemList.add(new Item("Label 56 \n Osama Bin Laden",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.oswaldo_paya);
        itemList.add(new Item("Label 57 \n Oswaldo Paya",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.oxana_fedorova);
        itemList.add(new Item("Label 58 \n Oxana Fedorova",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.padraig_harrington);
        itemList.add(new Item("Label 59 \n Padraig Harrington",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pamela_anderson);
        itemList.add(new Item("Label 60 \n Pamela Anderson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.patricia_clarkson);
        itemList.add(new Item("Label 61 \n Patricia Clarkson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.patty_schnyder);
        itemList.add(new Item("Label 62 \n Patty Schnyder",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.paula_radcliffe);
        itemList.add(new Item("Label 63 \n Paula Radcliffe",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.paul_tagliabue);
        itemList.add(new Item("Label 64 \n Paul Tagliabue",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pedro_solbes);
        itemList.add(new Item("Label 65 \n Pedro Solbes",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.princess_caroline);
        itemList.add(new Item("Label 66 \n Princess Caroline",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.prince_charles);
        itemList.add(new Item("Label 67 \n Prince Charles",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.prince_claus);
        itemList.add(new Item("Label 68 \n Prince Claus",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pupi_avati);
        itemList.add(new Item("Label 69 \n Pupi Avati",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.queen_beatrix);
        itemList.add(new Item("Label 70 \n Queen Beatrix",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.queen_latifah);
        itemList.add(new Item("Label 71 \n Queen Latifah",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.queen_rania);
        itemList.add(new Item("Label 72 \n Queen Rania",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rachel_hunter);
        itemList.add(new Item("Label 73 \n Rachel Hunter",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rafael_ramirez);
        itemList.add(new Item("Label 74 \n Rafael Ramirez",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rainer_schuettler);
        itemList.add(new Item("Label 75 \n Rainer Schuettler",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.raoul_ruiz);
        itemList.add(new Item("Label 76 \n Raoul Ruiz",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rebecca_romijn_stamos);
        itemList.add(new Item("Label 77 \n Rebecca Romijn-Stamos",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rebekah_chantay_revels);
        itemList.add(new Item("Label 78 \n Rebekah Chantay Revels",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.reese_witherspoon);
        itemList.add(new Item("Label 79 \n Reese Witherspoon",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ricardo_sanchez);
        itemList.add(new Item("Label 80 \n Ricardo Sanchez",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rick_carlisle);
        itemList.add(new Item("Label 81 \n Rick Carlisle",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rick_perry);
        itemList.add(new Item("Label 82 \n Rick Perry",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rick_pitino);
        itemList.add(new Item("Label 83 \n Rick Pitino",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rita_wilson);
        itemList.add(new Item("Label 84 \n Rita Wilson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.roberto_carlos);
        itemList.add(new Item("Label 85 \n Roberto Carlos",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.robert_de_niro);
        itemList.add(new Item("Label 86 \n Robert De Niro",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.robert_kocharian);
        itemList.add(new Item("Label 87 \n Robert Kocharian",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.robert_mueller);
        itemList.add(new Item("Label 88 \n Robert Mueller",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rob_lowe);
        itemList.add(new Item("Label 89 \n Rob Lowe",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rob_marshall);
        itemList.add(new Item("Label 90 \n Rob Marshall",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ronaldo_luis_nazario_de_lima);
        itemList.add(new Item("Label 91 \n Ronaldo Luis Nazario De Lima",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.roy_williams);
        itemList.add(new Item("Label 92 \n Roy Williams",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.russell_simmons);
        itemList.add(new Item("Label 93 \n Russell Simmons",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sachiko_yamada);
        itemList.add(new Item("Label 94 \n Sachiko Yamada",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sally_field);
        itemList.add(new Item("Label 95 \n Sally Field",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sandra_bullock);
        itemList.add(new Item("Label 96 \n Sandra Bullock",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sarah_hughes);
        itemList.add(new Item("Label 97 \n Sarah Hughes",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sarah_jessica_parker);
        itemList.add(new Item("Label 98 \n Sarah Jessica Parker",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.scott_mcclellan);
        itemList.add(new Item("Label 99 \n Scott Mcclellan",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.scott_peterson);
        itemList.add(new Item("Label 100 \n Scott Peterson",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sean_okeefe);
        itemList.add(new Item("Label 101 \n Sean Okeefe",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sebastien_grosjean);
        itemList.add(new Item("Label 102 \n Sebastien Grosjean",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sepp_blatter);
        itemList.add(new Item("Label 103 \n Sepp Blatter",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sergei_ivanov);
        itemList.add(new Item("Label 104 \n Sergei Ivanov",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sharon_stone);
        itemList.add(new Item("Label 105 \n Sharon Stone",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sheila_copps);
        itemList.add(new Item("Label 106 \n Sheila Copps",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sila_calderon);
        itemList.add(new Item("Label 107 \n Sila Calderon",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.silvan_shalom);
        itemList.add(new Item("Label 108 \n Silvan Shalom",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.slobodan_milosevic);
        itemList.add(new Item("Label 109 \n Slobodan Milosevic",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sourav_ganguly);
        itemList.add(new Item("Label 110 \n Sourav Ganguly",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.steffi_graf);
        itemList.add(new Item("Label 111 \n Steffi Graf",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.steve_lavin);
        itemList.add(new Item("Label 112 \n Steve Lavin",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.steve_nash);
        itemList.add(new Item("Label 113 \n Steve Nash",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.susan_sarandon);
        itemList.add(new Item("Label 114 \n Susan Sarandon",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.susilo_bambang_yudhoyono);
        itemList.add(new Item("Label 115 \n Susilo Bambang Yudhoyono",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tarig_aziz);
        itemList.add(new Item("Label 116 \n Tarig Aziz",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.thabo_mbeki);
        itemList.add(new Item("Label 117 \n Thabo Mbeki",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.thaksin_shinawatra);
        itemList.add(new Item("Label 118 \n Thaksin Shinawatra",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.thomas_fargo);
        itemList.add(new Item("Label 119 \n Thomas Fargo",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.time_haas);
        itemList.add(new Item("Label 120 \n Time Haas",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tim_allen);
        itemList.add(new Item("Label 121 \n Tim Allen",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tim_duncan);
        itemList.add(new Item("Label 122 \n Tim Duncan",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tim_robbins);
        itemList.add(new Item("Label 123 \n Tim Robbins",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.todd_haynes);
        itemList.add(new Item("Label 124 \n Todd Haynes",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tom_craddick);
        itemList.add(new Item("Label 125 \n Tom Craddick",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tom_crean);
        itemList.add(new Item("Label 126 \n Tom Crean",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tom_harkin);
        itemList.add(new Item("Label 127 \n Tom Harkin",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tony_bennett);
        itemList.add(new Item("Label 128 \n Tony Bennett",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tony_shalhoub);
        itemList.add(new Item("Label 129 \n Tony Shalhoub",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tony_stewart);
        itemList.add(new Item("Label 130 \n Tony Stewart",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.valentino_rossi);
        itemList.add(new Item("Label 131 \n Valentino Rossi",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.valery_giscard_destaing);
        itemList.add(new Item("Label 132 \n Valery Giscard Destaing",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vanessa_incontrada);
        itemList.add(new Item("Label 133 \n Vanessa Incontrada",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vanessa_redgrave);
        itemList.add(new Item("Label 134 \n Vanessa Redgrave",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vicente_fernandez);
        itemList.add(new Item("Label 135 \n Vicente Fernandez",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.victoia_clarke);
        itemList.add(new Item("Label 136 \n Victoia Clarke",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vince_carter);
        itemList.add(new Item("Label 137 \n Vince Carter",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vitali_klitschko);
        itemList.add(new Item("Label 138 \n Vitali Klitschko",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wayne_ferreira);
        itemList.add(new Item("Label 139 \n Wayne Ferreira",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wayne_gretzky);
        itemList.add(new Item("Label 140 \n Wayne Gretzky",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.william_bulger);
        itemList.add(new Item("Label 141 \n William Bulger",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.william_macy);
        itemList.add(new Item("Label 142 \n William Macy",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.win_aung);
        itemList.add(new Item("Label 143 \n Win Aung",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wolfgang_schuessel);
        itemList.add(new Item("Label 144 \n Wolfgang Schuessel",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.woodly_allen);
        itemList.add(new Item("Label 145 \n Woodly Allen",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xanana_gusmao);
        itemList.add(new Item("Label 146 \n Xanana Gusmao",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xavier_malisse);
        itemList.add(new Item("Label 147 \n Xavier Malisse",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yasar_yakis);
        itemList.add(new Item("Label 148 \n Yasar Yakis",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yevgeny_kafelnikov);
        itemList.add(new Item("Label 149 \n Yevgeny Kafelnikov",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yoko_ono);
        itemList.add(new Item("Label 150 \n Yoko Ono",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yu_shyi_kun);
        itemList.add(new Item("Label 151 \n Yu Shyi-kun",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zhang_ziyi);
        itemList.add(new Item("Label 152 \n Zhang Ziyi",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zinedine_zidane);
        itemList.add(new Item("Label 153 \n Zinedine Zidane",bitmap));

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zoran_djindjic);
        itemList.add(new Item("Label 154 \n Zoran Djindjic",bitmap));
    }

    // Hàm đổi avt, gọi intent để chọn cách đổi từ camera hoặc gallery
    public void ChangeAVTOnclick(View view) {
        Intent avatar = new Intent(this, ahihi.getClass());
        startActivityForResult(avatar, MY_AVATAR_KEY);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_AVATAR_KEY) { // Nhận result cho việc chọn cách đổi avt
            if (resultCode == File.RESULT_OK) {
                String reply = data.getStringExtra(File.MY_REPLY);
                if (reply.equals("camera"))
                    UseCamera();
                else UseGallery();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {  // Nhận result cho cách đổi avt bằng camera
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ImageView imgView = (ImageView) findViewById(R.id.avatar);
            imgView.setImageBitmap(imageBitmap);

        } else if (requestCode == RESULT_LOAD_IMAGE) {  // Nhận result cho cách đổi avt bằng gallery
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.avatar);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                imageBitmap = BitmapFactory.decodeFile(picturePath);

            }
        }
        //centroids = getCentroids();
        System.out.println("Num of centroids: " + centroids.size());
        faceCrop(imageBitmap);
    }

        //faceCompare(bitmapCrop);

////        String dataline = "";
////        for (int i = 0; i < embedding[0].length; i++)
////            dataline += embedding[0][i];
////        try {
////            kmeans.writeToFile(filename, dataline, true, true);
////            System.out.println("Successful write to file.");
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        }
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            centroids = getCentroids();
//            System.out.println("Num of centroids: " + centroids.size());
////        }
//        try {
//            String label = "";
//            float dis = 0.0f;
//            Centroid nearest = nearestCentroid(embedding, centroids);
//            if (nearest != null) {
//                label = nearest.getNum();
//                dis = kmeans.getDistance(embedding, nearest.getCoordinates());
//                System.out.print("Class: " + label + " - Distance: " + dis);
//            }
//            TextView result = findViewById(R.id.result);
//            result.setText("Class: " + label + " - Distance: " + dis);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    // Hàm đổi avt bằng gallery
    private void UseGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.PERMISSION_REQUESS);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }

    // Hàm đổi avt sử dụng camera
    private void UseCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAPTURE);
        }
        takePictureIntent();
    }

    // Intent chụp ảnh camera
    private void takePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            //Them ham luu anh vao day
        } catch (ActivityNotFoundException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAPTURE)          // Dành cho chụp ảnh
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        takePictureIntent();
                    }
                }
            }
        if (requestCode == PERMISSION_REQUESS)      // Dành cho lấy từ gallery
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Intent ii = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(ii, RESULT_LOAD_IMAGE);
                    } else Toast.makeText(this, "Permissinog denied...", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void faceCrop(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(this, "Choose an image", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap bitmapTemp1 = bitmap.copy(bitmap.getConfig(), true);
        Bitmap bitmapTemp2 = bitmap.copy(bitmap.getConfig(), true);

        Vector<Box> boxes1 = detect.detectFaces(bitmapTemp1, bitmapTemp1.getWidth() / 5);
        System.out.println("Num of faces: ");
        System.out.println(boxes1.size());
        for (int i = 0; i < boxes1.size(); i++) {
            Box box1 = boxes1.get(i);

            box1.toSquareShape();
            box1.limitSquare(bitmapTemp1.getWidth(), bitmapTemp1.getHeight());
            Rect rect1 = box1.transform2Rect();

            Util.drawBox(bitmapTemp1, box1, 5);
            ImageView imageView = (ImageView) findViewById(R.id.avatar);
            imageView.setImageBitmap(bitmapTemp1);
        }

        //Align faces
        Box box1 = boxes1.get(0);
        box1.toSquareShape();
        box1.limitSquare(bitmapTemp2.getWidth(), bitmapTemp2.getHeight());
        Rect rect1 = box1.transform2Rect();
        bitmapCrop = MyUtil.crop(bitmapTemp2, rect1);
        //bitmapCrop = Align.face_align(bitmapCrop, box1.landmark);

        ImageView imageView = (ImageView) findViewById(R.id.face);
        imageView.setImageBitmap(bitmapCrop);

        faceCompare(bitmapCrop);
        Centroid nearest = null;
//        borderedText = new BorderedText(15);
//        final Canvas canvas = new Canvas(bitmapCrop);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setFilterBitmap(true);
//        paint.setDither(true);
//        paint.setColor(Color.RED);
        try {
            Box box;
            Bitmap temp;
            Bitmap drawText = bitmapTemp1;
            // Loop through box in boxes1
            for (int i = 0; i < boxes1.size(); i++) {

                box = boxes1.get(i);
                box.toSquareShape();
                box.limitSquare(bitmapTemp2.getWidth(), bitmapTemp2.getHeight());
                Rect rect = box.transform2Rect();
                temp = MyUtil.crop(bitmapTemp2, rect);
                //temp = Align.face_align(temp, box.landmark);
                faceCompare(temp);
                nearest = nearestCentroid(embedding, centroids);
                if (nearest != null) {
                    //result.setText("Class: " + nearest.getNum() + " - Distance: " + KMeansOnline.getDistance(embedding, nearest.getCoordinates()));
                    //drawText = drawTextToBitmap(context, drawText, nearest.getNum(), rect);
//                    ImageView imageView1 = (ImageView) findViewById(R.id.avatar);
//                    imageView1.setImageBitmap(drawText);
                } else {
//                    EditText editText = (EditText) findViewById(R.id.textview);
//                    text = editText.getText().toString();
                    if (text != null) {
                        Centroid cent = new Centroid(text, embedding);
                        centroids.add(cent);
                        //result.setText("Not match with any classes in dataset");

//                    ImageView imageView1 = (ImageView) findViewById(R.id.avatar);
//                    imageView1.setImageBitmap(drawText);
                    }
                    //drawText = drawTextToBitmap(context, drawText, "Unknown", rect);
                }
                ImageView imageView1 = (ImageView) findViewById(R.id.avatar);
                imageView1.setImageBitmap(drawText);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void faceCompare(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(this, "Choose an image", Toast.LENGTH_LONG).show();
            return;
        }

        embedding = mfn.getEmbedding(bitmap);
//        for (int i = 0; i < embedding[0].length; i++) {
//            System.out.print(embedding[0][i] + " ");
//        }
    }

    public ArrayList<Centroid> getCentroids() {
        int n = 1;
        String count;
        BufferedReader reader = null;
        BufferedWriter bw;
        count = "";
        ArrayList<Centroid> listCenTroid = new ArrayList<Centroid>();
//        try {
////            InputStream assetIs = getAssets().open("labelmap.txt");
////            OutputStream copyOs = openFileOutput("labelmap.txt", MODE_PRIVATE);
////            byte[] buffer = new byte[4096];
////            int bytesRead;
////            while ((bytesRead = assetIs.read(buffer)) != -1) {
////                copyOs.write(buffer, 0, bytesRead);
////            }
////            assetIs.close();
////            copyOs.close();
//            // now you can open and modify the copy
//            OutputStream copyOs = getApplication().openFileOutput("labelmap.txt", MODE_APPEND);
//
//            BufferedWriter writer =
//                    new BufferedWriter(
//                            new OutputStreamWriter(copyOs));
//            String strings = "";
//            for (int i = 0; i < listCenTroid.size(); i++){
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("labelmap.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String id = "";
                String vector = "";
                if (n % 2 == 1) {
                    id = mLine;
                    n++;
                }
                if (n % 2 == 0){
                    vector = reader.readLine();
                }
                Centroid cenTroid = new Centroid(id, readCentroid(vector));
                listCenTroid.add(cenTroid);
                n++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return listCenTroid;
    }

    public Centroid nearestCentroid(float[][] emb, ArrayList<Centroid> centroids) throws FileNotFoundException {
        float minimumDistance = 0.7f;
        Centroid nearest;
        Centroid temp = null;
        float currentDistance = 0.0f;


        for (Centroid centroid : centroids) {
            // Compute distance between centroid and embedding
            currentDistance = KMeansOnline.getDistance(emb, centroid.getCoordinates());
            System.out.println("Dis: " + currentDistance);

            if (currentDistance > minimumDistance) {
                minimumDistance = currentDistance;
                temp = centroid;
            }
        }

//        if (currentDistance <= threshold) {
//            nearest = temp;
        //float[][] temp_cent = computeCentroid(nearest.getCoordinates(), nearest.getNum());
        // Update centroid
//            updateCentroid(nearest, temp_cent);
//            // write to file
//            String num = String.valueOf(nearest.getNum() + 1);
//            String centroidString = "";
//            for (int i = 0; i < nearest.getCoordinates()[0].length; i++) {
//                centroidString += nearest.getCoordinates()[0][i] + " ";
//            }
//            // Update at specific line in txt
//        }
//        else {
//            // init new centroid
//            nearest = new Centroid();
//            n_clusters += 1;
//
//            // write to file
//            String num = String.valueOf(nearest.getNum() + 1);
//            String centroidString = "";
//            for (int i = 0; i < nearest.getCoordinates()[0].length; i++) {
//                centroidString += String.valueOf(nearest.getCoordinates()[0][i]) + " ";
//            }
//            boolean writeFileNum = writeToFile(FILE_NAME, num, true, true);
//            boolean writeFileString = writeToFile(FILE_NAME, centroidString, true, true);
//        }
        nearest = temp;
        return nearest;
    }

    public static float[][] readCentroid(String s) {
//        float[][] cent = new float[1][192];
//        List<String> list = Arrays.asList(s.split(" "));
//        for (int i = 0; i < 192; i++) {
//            cent[0][i] = Float.parseFloat(list.get(i));
//        }
//        return cent;
        float[][] cent = new float[1][192];
        String [] strings = s.split("\\s");
        int i = 0;
        for (String w : strings) {
            cent[0][i] = Float.parseFloat(w);
            i++;
        }
        return cent;
    }

    public Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap,
                                   String gText, Rect rect) {

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are immutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(0, 255, 0));
        // text size in pixels
        int minimum = Math.min(width, height);
        float temp = (float) (0.1 * minimum);
        paint.setTextSize((int) (temp));
        // text shadow
        paint.setShadowLayer(5f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = rect.left;
        int y = rect.bottom + 17;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }
}