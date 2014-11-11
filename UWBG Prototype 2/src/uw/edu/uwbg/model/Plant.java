package uw.edu.uwbg.model;

import java.util.Map;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

public class Plant  {
	
	public String UWBGAccession;
	public String plantName;
	public String scientificName;
	public String family;
	public String familyCommonName;
	public String genus;
	public String cultivarName;
	public String source;
	public String mapGrid;
	public String HerbariumSpecimen;
	public String lastReportedCondition;
	public String epithet;
	public String commonName;
	public Point geom=null;
	
	public void setAttributes(Graphic graphic){
		 plantName=(String) graphic.getAttributeValue("Plants.Name");
         family=(String) graphic.getAttributeValue("BGBaseData.Family");
         familyCommonName=(String) graphic.getAttributeValue("BGBaseData.FamilyCommonName");
         epithet=(String) graphic.getAttributeValue("BGBaseData.SpecificEpithet");
         genus=(String) graphic.getAttributeValue("Plants.Genus");
         HerbariumSpecimen=(String) graphic.getAttributeValue("BGBaseData.HerbariumVoucherNumber");
         lastReportedCondition=(String) graphic.getAttributeValue("BGBaseData.Condition");
         mapGrid=(String) graphic.getAttributeValue("BGBaseData.Grid");
         scientificName=(String) graphic.getAttributeValue("BGBaseData.ScientificName");
         source=(String) graphic.getAttributeValue("BGBaseData.SourceCity");
         UWBGAccession=(String) graphic.getAttributeValue("Plants.Accession");
         commonName=(String) graphic.getAttributeValue("BGBaseData.CommonName");
         geom=(Point) graphic.getGeometry();
         
	}
	public void setAttributesmap(Map hm,Geometry g){
		 plantName=(String) hm.get("Plants.Name");
        family=(String)hm.get("BGBaseData.Family");
        familyCommonName=(String) hm.get("BGBaseData.FamilyCommonName");
        epithet=(String) hm.get("BGBaseData.SpecificEpithet");
        genus=(String) hm.get("Plants.Genus");
        HerbariumSpecimen=(String) hm.get("BGBaseData.HerbariumVoucherNumber");
        lastReportedCondition=(String) hm.get("BGBaseData.Condition");
        mapGrid=(String) hm.get("BGBaseData.Grid");
        scientificName=(String) hm.get("BGBaseData.ScientificName");
        source=(String) hm.get("BGBaseData.SourceCity");
        UWBGAccession=(String) hm.get("Plants.Accession");
        commonName=(String) hm.get("BGBaseData.CommonName");
        geom=(Point) g;
	}
	public void setGeometry(Geometry g){
		geom=(Point) g;
	}

}
