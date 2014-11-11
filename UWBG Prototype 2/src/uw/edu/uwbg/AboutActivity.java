package uw.edu.uwbg;

/**
Copyright � <2014> <University of Washington>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

import uw.edu.uwbg.helper.CustomFont;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Displays the text from the FAQs page of BG web site.
 * @author 	Brett Schormann
 * @version 0.1 10/2014
 * 			0.2 10/31/2014
 * 			Used CustomFont to set up text.
 * @since 	0.1		
 *
 */
public class AboutActivity extends Activity {
	private final String htmlText = "<p><strong>The University of Washington Botanic Gardens (UWBG) is " +
			"part of the School of Environmental and Forest Sciences in the College of the Environment.</strong></p>" +
			"<p>The Botanic Gardens&rsquo; name was established in 2005 to unite the gardens and programs of the " +
			"Washington Park Arboretum and the&nbsp; Center for Urban Horticulture and is located around the shoreline " +
			"of Union Bay on Lake Washington.</p>" +
			"<p>UWBG <strong>Living Plant Collections </strong>contain 11,033 specimens representing over 4,000 distinct taxa. " +
			"2,308 accessions are of known  wild origin.</p>" + 
			"<p>The 230-acre <strong>Washington Park Arboretum</strong> is one of the most important tree collections " +
			"in North America. It is jointly managed by UWBG (plant collections) and the City of Seattle&rsquo;s Department of " +
			"Parks and Recreation (parkfunctions), with support from the Arboretum Foundation. WPA is in the top five " +
			"national arboreta and botanic gardens collections in maples, magnolias, Pine family, hollies, mountain ash, oaks " +
			"and viburnums and is currently a member of the North American Plant Collections Consortium (NAPCC) for maples and oaks.</p>" +
			"<p> All parts of the Washington Park Arboretum, with the exception of the Japanese Garden, are <strong>open to the " +
			"public free of charge.</strong></p>" +
			"<p>The 16-acre <strong>Center for Urban Horticulture </strong>site serves as the meeting place  for " +
			"horticultural groups. The Center&rsquo;s Merrill Hall is the first sustainable building to be built on the " +
			"UW Seattle campus. It houses our administrative offices and research labs, the Miller Library, and the Hyde Herbarium. " +
			"It also provides space to the WSU King County Extension Program and the Master Gardener  Foundation of King County. " +
			"Gardens at the Center contain over 150 woody plant species and cultivated varieties and nearly 500 herbaceous perennials.</p>" +
			"<p>The Center for Urban Horticulture also includes the 74-acre <strong>Union Bay Natural Area</strong>.  With " +
			"four miles of shoreline, it serves as an outdoor laboratory for UW research and as a publicly accessible wildlife " +
			"habitat.&nbsp; More than 200 bird species have been sighted in UBNA.</p>" +
			"<p> The <strong>Elisabeth C. Miller Library</strong> is one of the most important horticultural library in North America. " +
			"It houses 15,500 books, 500 journal and  periodical titles, 1000 nursery catalogs, and video and electronic resources. " +
			"It offers a range of free services to the&nbsp; public and the academic community. The annual Garden Lovers&rsquo; " +
			"Book Sale is held in early April.</p>" +
			"<p>The <strong>Otis Douglas Hyde Herbarium</strong> houses over 20,000 specimens from the Washington Park Arboretum " +
			"and elsewhere and is one of the nation&rsquo;s largest collection of preserved, cultivated plants. " +
			"It serves as the official herbarium for the Washington State Noxious Weed Board and also provides free plant " +
			"identification help to the public.</p>" +
			"<p>The Botanic Gardens emphasize <strong>sustainable practices </strong>throughout our programs. " +
			"In addition to composting, recycling, and using electric work carts, we boast the first LEED-certified building " +
			"on campus, practice Integrated Pest Management, and contribute to UW&rsquo;s Salmon-Safe certification.</p>" +
			"<p>Number of People who visit annually (Admission is free): 320,000 (250,000 to the Arboretum; 70,000 to " +
			"the Center), 98% are from  Washington State.</p>";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        TextView htmlTextView = (TextView)findViewById(R.id.about);
		
        CustomFont customFont = CustomFont.getCustomFont();
		customFont.setTextViewParameters(this, htmlTextView, "OpenSans-Regular", "MEDIUM_TEXT_SIZE", htmlText, true);		
	}
}

