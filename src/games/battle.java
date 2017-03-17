package games;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class battle {
	Pokemon P1[];
	Pokemon P2[];
	int pIndex1, pIndex2;
	
	public void main() throws IOException {
		Scanner sc = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		

		//INICIO -- Leer Pokedex
		String nombArch = "pokedex.json";		
		JsonParser parser = new JsonParser();

		try {
			FileReader fr = new FileReader(nombArch);
			JsonElement datos = parser.parse(fr);
			
			JsonArray arreglo = datos.getAsJsonArray();
			
			for (int i=0; i<arreglo.size(); i++){
				//Lee los nombres
				JsonObject obj = arreglo.get(i).getAsJsonObject();
				String name = obj.get("Name").getAsString();
				System.out.println((i+1) + ". " + name);
			}	
			fr.close();
		} catch (FileNotFoundException ex) {

		}
		catch (Exception ex) {

		}		
		//FIN -- Leer POkEDEX
		System.out.println("\nEscoge batalla de cuantos vs cuantos");
		int num_pokes= sc.nextInt();
		
		System.out.println("\nEscoge habilidad de computadora\n1. Aleatorio\n2. Minimax?");
		int modo_juego= sc.nextInt();	
		
		//INICIO -- Elegir pokemons
		P1 = new Pokemon[num_pokes];
		P2 = new Pokemon[num_pokes];
		Random rnd = new Random();		
		pIndex1 = 0;
		pIndex2 = 0;
		System.out.println("\nEscoge tus " + num_pokes + " Pokemon");
		for (int i=0; i<num_pokes; i++){
			System.out.println("Pokemon " + (i+1) + ":");
			int p1 = sc.nextInt();												
			P1[i] = new Pokemon(p1);
			
			//Elegir los pokemons enemigos aleatoriamente (Random del 1 al 20)			
			int p2 = rnd.nextInt(19) + 1; 			
			P2[i] = new Pokemon(p2);
		}
		//FIN -- Elegir pokemons
					
		
		
		System.out.println("\nQue comienze la batalla!!!");
		
			Pokemon Poke1, Poke2;		
			
		//INICIO -- Batalla pokemon
		while(!ganador(P1,P2,num_pokes)){
			//Se define qué pokemon ataca primero según su velocidad (SPE)
			if (P1[pIndex1].Spe > P2[pIndex2].Spe) {
				Poke1 = P1[pIndex1];
				Poke2 = P2[pIndex2];
				System.out.print("JUGADOR ");
			} else {
				Poke1 = P2[pIndex2];
				Poke2 = P1[pIndex1];
				System.out.print("PC's ");
			}
			System.out.println(Poke1.name + " empieza");

			//Datos y movimientos del Pokemon del jugador
			System.out.println("\nJUGADOR:\n" + P1[pIndex1].name);
			System.out.println("HP:" + P1[pIndex1].HP + "  Att: " + P1[pIndex1].Att + "  Def: " + P1[pIndex1].Def + "\n");
			System.out.println("MOVIMIENTOS:");
			P1[pIndex1].printMoves();
			
			//Datos de Pokemon enemigo
			System.out.println("\nPC:\n" + P2[pIndex2].name);
			System.out.println("HP:" + P2[pIndex2].HP + "  Att: " + P2[pIndex2].Att + "  Def: " + P2[pIndex2].Def + "\n");
			System.out.println("\n" + P1[pIndex1].name + ", escoge un movimiento (numero)");
			int m, mov1, mov2;
			m = sc.nextInt();
			m--;								
			
			int pIndexNew = 0;
			wrapInt pIndexNewPC = new wrapInt();
			
			if (Poke1==P1[pIndex1]){
				mov1 = m;
				if(modo_juego==1) mov2 = P2[pIndex2].movAleatorio(P2, num_pokes, pIndexNewPC);
				else mov2 =P2[pIndex2].heuristica1(P1[pIndex1], P2[pIndex2], P1, P2, num_pokes);
			}
			else{ 
				mov2 = m;
				if(modo_juego==1) mov1 = P2[pIndex2].movAleatorio(P2, num_pokes, pIndexNewPC);
				else mov1 =P2[pIndex2].heuristica1(P1[pIndex1], P2[pIndex2], P1, P2, num_pokes);
			}
			
			//Si se elige cambiar de Pokemon (opcion 5 (m = 4) )				
			if (m == 4){				
				System.out.println("\nJugador escoge un Pokemon:");
				for (int i=0; i<num_pokes; i++){
					System.out.print((i+1) + ". ");
					System.out.println(P1[i].name + " - HP: " + P1[i].HP);															
				}			
				pIndexNew = sc.nextInt();
				pIndexNew--;
			}
			
			if(Poke1.HP > 0){
				//Si se elige cambiar de Pokemon (opcion 5 (mov1 = 4) )
				if (mov1>=4)
					if (Poke1 == P1[pIndex1]){							
						System.out.println("Regresa " + Poke1.name + "!");
						pIndex1 = pIndexNew;
						Poke1 = P1[pIndex1];
						System.out.println("Yo te elijo " + Poke1.name + "!");
						}
					else {
						if(modo_juego==1){
							System.out.println("PC regreso a " + Poke1.name + "!");
							pIndex2 = pIndexNewPC.Num;
							Poke1 = P2[pIndex2];
							System.out.println("PC envio a " + Poke1.name + "!");
						}
						else{
							System.out.println("PC regreso a " + Poke1.name + "!");
							pIndexNewPC.Num=mov1-4;
							pIndex2 = pIndexNewPC.Num;
							Poke1 = P2[pIndex2];
							System.out.println("PC envio a " + Poke1.name + "!");
						}
					}
				else
					Poke1.calculateDamage(Poke2, mov1);
			}
			if (Poke2.HP <= 0) {
				System.out.println(Poke2.name + " ha sido derrotado! \n" + Poke1.name + " gana!");
				Poke2.HP = 0;
				if (ganador(P1, P2, num_pokes))
					break;
					
				if (Poke2==P1[pIndex1]){					
					System.out.println("\nElige a un Pokemon:");
					for (int i=0; i<num_pokes; i++){
						if (P1[i].HP>0){
							System.out.print((i+1) + ". ");
							System.out.println(P1[i].name + " - HP: " + P1[i].HP);
						}															
					}			
					pIndexNew = sc.nextInt();
					pIndexNew--;
					pIndex1 = pIndexNew;					
				}
				else{			
					while (true){
						pIndexNewPC.Num = rnd.nextInt(num_pokes);
						if (P2[pIndexNewPC.Num].HP>0)
							break;
					}
					pIndex2 = pIndexNewPC.Num; 					
				}
			}

			if(Poke2.HP > 0){
				//Si se elige cambiar de Pokemon (opcion 5 (mov1 = 4) )
				if (mov2>=4)
					if (Poke2 == P1[pIndex1]){
						System.out.println("Regresa " + Poke2.name + "!");
						pIndex1 = pIndexNew;
						Poke2 = P1[pIndex1];
						System.out.println("Yo te elijo " + Poke2.name + "!");
						}
					else{
						if(modo_juego==1){
							System.out.println("PC regreso a  " + Poke2.name + "!");
							pIndex2 = pIndexNewPC.Num;
							Poke2 = P2[pIndex2];
							System.out.println("PC envio a " + Poke2.name + "!");
						}						
						else{
							System.out.println("PC regreso a " + Poke2.name + "!");
							pIndexNewPC.Num=mov2-4;
							pIndex2 = pIndexNewPC.Num;
							Poke2 = P2[pIndex2];
							System.out.println("PC envio a  " + Poke2.name + "!");
						}
					}
				else
				Poke2.calculateDamage(Poke1, mov2);
			}
			if (Poke1.HP <= 0) {
				System.out.println(Poke1.name + " ha sido derrotado! \n" + Poke2.name + " gana!");
				Poke1.HP = 0;
				if (ganador(P1, P2, num_pokes))
					break;
				
				if (Poke1==P1[pIndex1]){
					System.out.println("\nEscoge a un Pokemon:");
					for (int i=0; i<num_pokes; i++){
						if (P1[i].HP>0){
							System.out.print((i+1) + ". ");
							System.out.println(P1[i].name + " - HP: " + P1[i].HP);
						}															
					}			
					pIndexNew = sc.nextInt();
					pIndexNew--;
					pIndex1 = pIndexNew;										
				}
				else{
					while (true){
						pIndexNewPC.Num = rnd.nextInt(num_pokes);
						if (P2[pIndexNewPC.Num].HP>0)
							break;
					}
					pIndex2 = pIndexNewPC.Num;					
				}
			}				
			
			System.out.println("\n" + P1[pIndex1].name + "\nHP:" + P1[pIndex1].HP + "\n");
			System.out.println("\n" + P2[pIndex2].name + "\nHP:" + P2[pIndex2].HP + "\n");
			System.out.println("\n**Ingresa una tecla para continuar**");
			sc.next();
			System.out.print("\n\n");
		}
		//FIN -- Batalla pokemon
		
		System.out.println("Quieres pelear de nuevo?(y/n)");
		char c = (br.readLine().toLowerCase()).charAt(0);
		if (c == 'y')
			main();
		else {
			System.out.println("Gracias por jugar!!!");
			sc.close();
			System.exit(0);
		}		
	}


	private boolean ganador(Pokemon[] p1, Pokemon[] p2,int num_pokes) {
		boolean gano_persona=false;
		int numP1=0, numP2=0;
		for(int i=0;i<num_pokes;++i){
			if (p1[i].HP>0)
				numP1++;
			if (p2[i].HP>0)
				numP2++;
		}
							
		if (numP1==0){
			gano_persona=true;
			System.out.println("PC GANO!");
			}
		if (numP2==0){
			gano_persona=true;
			System.out.println("EL JUGADOR GANO!");
		}
		return gano_persona;
	}
		
}

