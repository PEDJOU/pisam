package com.moov.moovservice.utils;

import lombok.experimental.UtilityClass;

import static com.moov.moovservice.utils.Constants.PLUS;
import static com.moov.moovservice.utils.Constants.POINT;
import static com.moov.moovservice.utils.Constants.DEUX_POINT;
import static com.moov.moovservice.utils.Constants.TIRET;
import static com.moov.moovservice.utils.Constants.VIDE;

import org.apache.commons.lang3.StringUtils;



@UtilityClass
public class UtilsGenerale {
	

	public static String tranformTelephone(String telephone) {
		//Supprimer le caractère spéciaux
		telephone = telephone.replace("+", "").replace(".", "").replace("-", "");
		//Verifions le préfixe du numéro
        var startsWithPrefixe01 = telephone.substring(0, 3).equals("225");
		var startsWithPrefixe02 = telephone.substring(0, 4).equals("0225");
		var startsWithPrefixe03 = telephone.substring(0, 5).equals("00225");
		
       if((!startsWithPrefixe01 && startsWithPrefixe02 && startsWithPrefixe03) || (telephone.length() == 10 &&  !telephone.startsWith("225")) ) return "225".concat(telephone);

		return startsWithPrefixe02 ?
						telephone.replace("0225", "225"): 
							(startsWithPrefixe03 ? telephone.replace("00225", "225") : telephone );
	}
  
	
	
	
	
	
	public static String creerIdentifiant(String date,String telephone) {
		//Supprimer le caractère spéciaux
		date.replace(DEUX_POINT, VIDE).replace(TIRET, VIDE);
		telephone.replace(PLUS, VIDE).replace(POINT, VIDE).replace(TIRET, VIDE);
		return StringUtils.deleteWhitespace(date.concat(telephone));
	}


}
