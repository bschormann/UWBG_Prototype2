package uw.edu.uwbg;


import uw.edu.uwbg.sheymafragments.BookMarkListFragment;
import uw.edu.uwbg.sheymafragments.PlantLookUpFragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FragmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		PlantLookUpFragment plantlookupfrag=new PlantLookUpFragment(null);
		BookMarkListFragment bookmarkfrag=new BookMarkListFragment();
		
		
		int code=getIntent().getExtras().getInt("code");
		
		switch(code){
		case 1:
			getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, bookmarkfrag).
	    	commit();			
			break;
		default:
			getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, plantlookupfrag).
	    	commit();
	    	break;
		}
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.second_menue, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
	    case R.id.home:
	    	finish();
	    	break;
	    }
		return true;
	}
	
	
}
