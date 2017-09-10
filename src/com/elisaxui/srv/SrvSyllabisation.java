/**
 * 
 */
package com.elisaxui.srv;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.elisaxui.core.data.json.JSONBuilder;

import javax.ws.rs.core.UriInfo;

/**
 * @author Bureau
 *
 */

@Path("/json")
public class SrvSyllabisation {

	@GET
	@Path("/syllabisation/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSyllabisation(@Context HttpHeaders headers, @Context UriInfo uri, @PathParam("id") String id) {

		Object json = doSyllbisation();
		
		return Response.status(Status.OK)
				.entity(json.toString())
				.build();
	}

	public Object doSyllbisation() {
	
		class jsonMot extends JSONBuilder
		{
			String unePhrase = "Il était une fois une jeune princesse intrépide qui s'appelait Pocahontas.\n "
					+ "Elle aimait parcourir ses terres natales en toute liberté.\n"
					+ "Pocahontas et son amie, Nakoma, passaient des heures entières à explorer.\n "
					+ "Descendant la rivière à bord de leur canoë, elles admiraient les paysages environnants.\n"
//					+"un portefeuille un feuilleton un chevreuil\n"
//					+ "s’habiller famille gorille incroyable\n"
//					+ "épouvantail bataille travailler débarbouiller\n"
//					+ "ratatouille bouilloire\n" + 
//					"Merveilleux surveiller  corbeille appareil\n" + 
//					"heureuse mignonne champignon campagne\n" + 
//					"pied poulet dessinez conjugaison anniversaire\n" + 
//					"directrice lunettes empreinte lendemain\n" + 
//					"\n" + 
//					"\n" + 
//					""
					;
			
			/* (non-Javadoc)
			 * @see com.elisaxui.core.data.json.JSONBuilder#getJSON()
			 */
			@Override
			public Object getJSON() {
				unePhrase= unePhrase.replace('\n', ' ');
				Scanner s = new Scanner(unePhrase).useDelimiter(" ");
				List<Object> jsonMot = new ArrayList<Object>();
				while (s.hasNext()) {
					String word = s.next();
					Mot unMot = new Mot(word);
					unMot.doSyllabisation();
					
					jsonMot.add( obj(v("text", word), v("syllabes", unMot.doJson())));
					
					//unMot.doDisplay();
				}
				s.close();
				//System.out.println("");
				return  obj(v("type", "phrase"), v("mots",  arr(jsonMot.toArray())));
			}
	

		}
		
		return new jsonMot().getJSON();
	}

	public static class Syllable {
		StringBuffer syllabe = new StringBuffer();
		boolean syllableSansVoyelle = true;

		public void add(char c) {
			syllabe.insert(0, c);
			if (syllableSansVoyelle)
				syllableSansVoyelle = !Mot.isVoyelle(c);
		}

		public int size() {
			return syllabe.length();
		}

		public char lastChar() {
			return syllabe.charAt(0);
		}

		public char avantDernierChar() {
			return syllabe.charAt(1);
		}
	}

	public static class Mot {

		StringBuffer mot = null;
		LinkedList<Syllable> listSyllable = new LinkedList<Syllable>();

		public int nbCar() {
			return mot.length();
		}

		public Mot(String mot) {
			super();
			this.mot = new StringBuffer(mot);
		}

		public char getChar(int i) {
			return mot.charAt(i);
		}

		boolean isVoyelle(int i) {
			char c = mot.charAt(i);
			return isVoyelle(c);
		}

		Syllable syllabe = new Syllable();

		public void doSyllabisation() {

			for (int i = nbCar() - 1; i >= 0; i--) {

				boolean isVoyelle = isVoyelle(i);
				int nbCarSyllabe = syllabe.size();
				char c = getChar(i);

				if (nbCarSyllabe == 0) {
					syllabe.add(c);
				} else {
					if (isVoyelle) {
						if (Mot.isVoyelle(syllabe.lastChar())) {
							boolean isVoyelleSeule = Mot.isVoyelleSeule(syllabe.lastChar());
							if (isVoyelleSeule) {
								createNewSyllabe(c);
							} else {
								syllabe.add(c);
							}
						} else {
							// decoupe voyelle / consonne
							if (nbCarSyllabe > 1) {

								if (!Mot.isVoyelle(syllabe.avantDernierChar())) {
									// la premiere consonne doit elle etre seule ?
									syllabe.add(c);
								} else {
									createNewSyllabe(c);
								}
							} else {
								syllabe.add(c);
							}
						}
					} else {
						if (Mot.isVoyelle(syllabe.lastChar())) {
							syllabe.add(c);
						} else {
							// decoupe entre 2 consonne
							// (i< mot.length()-2 && !Mot.isVoyelle(getChar(i+2)));
							// if (i==0 || (i< mot.length()-2 && (!Mot.isVoyelle(getChar(i+2)) ||
							// !Mot.isVoyelle(getChar(i-1))))) // une consonne ne doit pas etre seule
							if (nbCarSyllabe == 1 || i == 0 || (!Mot.isVoyelle(getChar(i - 1)))
									|| syllabe.syllableSansVoyelle) // une consonne ne doit pas etre seule
							{
								syllabe.add(c);
							} else {
								createNewSyllabe(c);
							}
						}
					}
				}
			}

			listSyllable.add(0, syllabe);
		}

		private void createNewSyllabe(char c) {
			listSyllable.add(0, syllabe);
			syllabe = new Syllable();
			syllabe.add(c);
		}

		public void doDisplay() {
			for (int i = 0; i < listSyllable.size(); i++) {
				if (i > 0)
					System.out.print("/");

				System.out.print(listSyllable.get(i).syllabe);
			}
			System.out.print(" ");
		}
		
		public Object doJson() {
			class JSONStyllable extends JSONBuilder
			{
				public Object getJSON()
				{
					List<Object> jsonSyllable = new ArrayList<Object>();
					for (int i = 0; i < listSyllable.size(); i++) {
						jsonSyllable.add(obj(v("text", listSyllable.get(i).syllabe.toString() )));
					}
					Object ret = arr(jsonSyllable.toArray());
					return ret;
				}
			}

			return new JSONStyllable().getJSON();
		}

		public static boolean isVoyelle(char c) {
			switch (c) {
			case 'A':
			case 'E':
			case 'I':
			case 'O':
			case 'U':
			case 'Y':
			case 'y':
			case 'a':
			case 'e':
			case 'é':
			case 'è':
			case 'ê':
			case 'ë':
			case 'i':
			case 'o':
			case 'u':
				return true;
			default:
				return false;
			}
		}

		public static boolean isVoyelleSeule(char c) {
			switch (c) {
			case 'é':
			case 'è':
			case 'ê':
			case 'ë':
				// case 'y':
				return true;
			default:
				return false;
			}

		}
	}

}