package myproject.avoid;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapter.AlbumsAdapter;
import Kelas.Album;

public class ListSayurPembeli extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    public static ProgressBar progressBar;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    Intent i;
    public static String keyPSayur,namaPsayur,phone;
    TextView txtNamaPSayur,txtPhone;
    FloatingActionButton btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sayur_pembeli);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initCollapsingToolbar();

        i = getIntent();
        keyPSayur = i.getStringExtra("key");
        namaPsayur = i.getStringExtra("nama");
        phone = i.getStringExtra("phone");

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(ListSayurPembeli.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txtNamaPSayur = (TextView) findViewById(R.id.txtNamaPSayur);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        btnCall = (FloatingActionButton) findViewById(R.id.btnCall);
        txtNamaPSayur.setText("Penjual : "+namaPsayur);
        txtPhone.setText("No. HP : "+phone);

        adapter = new AlbumsAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dial = "tel:" + phone;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        });
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
