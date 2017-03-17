package games;

import java.util.*;
import java.io.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Pokemon {
	int HP, Def, Att, Spe;
	String name;
	String moves[][];

	//Constructor
	Pokemon(int index){		
		String nombArch = "pokedex.json";		
		
		JsonParser parser = new JsonParser();

		try {
			FileReader fr = new FileReader(nombArch);
			JsonElement datos = parser.parse(fr);
			
			JsonArray arreglo = datos.getAsJsonArray();
			
			if (index>arreglo.size()){
				System.out.println("Input incorrecto!");
				System.exit(0);
			}
			
			//Lee los atributos del archivo JSON
			JsonObject obj = arreglo.get(index-1).getAsJsonObject();
			name = obj.get("Name").getAsString();
			HP = obj.get("HP").getAsInt();
			Def = obj.get("Def").getAsInt();
			Att = obj.get("Att").getAsInt();
			Spe = obj.get("Spe").getAsInt();
			
			moves=new String[4][3];
			
			double prob_moves[]=new double[8];
			//Definir los movimientos disponibles
			for(int i=0;i<8;i++)
				prob_moves[i]=Math.random();
			
			int movs[]=new int[4];
			
			for(int y=0;y<4;++y){
				double max=0;
				int mov_esc=0;
				for(int i=0;i<8;++i)				
					if(prob_moves[i]>max){
						max=prob_moves[i];					
						mov_esc=i;
					}				
				prob_moves[mov_esc]=0.0;
				movs[y]=mov_esc;
			}
			
			String token_moves[][]=new String [8][3];
			
			for (int i=0; i<8; i++)
				for (int j=0; j<3; j++)
					token_moves[i][j] = obj.get("moves"+i+j).getAsString();
					//moves[i][0] -> Nombre del movimiento
					//moves[i][1] -> Base Power
					//moves[i][2] -> Accuracy
					//moves[i][3] -> Type
					//moves[i][4] -> Other							
							
			for(int i=0;i<4;++i)
				for (int j=0; j<3; j++)
					moves[i][j] = token_moves[movs[i]][j];
			
		} catch (FileNotFoundException ex) {}		
	}
	
	void printMoves() {
		int i;
		for (i = 0; i < 4; i++)
			System.out.println((i + 1) + ". " + moves[i][0] + "\tBP: " + moves[i][1]);
		System.out.println((i+1) + ". Cambiar de pokemon");
	}

	void calculateDamage(Pokemon P, int m) {		
		double damage = 0.0;
		double r = Math.random() * 100;
		
		System.out.print("\n" + name + " uso " + moves[m][0] + "!");
		
		if (r > Double.parseDouble(moves[m][2])) {
			System.out.println("\nAtaque perdido!");
			return;
		}
		
		//Funcion para calcular daño
		damage = (1.0*Att/P.Def)*Double.parseDouble(moves[m][1]);
		System.out.format(" (Daño: %.0f)\n", damage);
		P.HP-=(int)damage;

		if (damage == 0)
			System.out.println(moves[m][0] + " no tiene efecto en " + P.name + "!");
	}
	
	public int movAleatorio(Pokemon[] Poke, int num_pokes, wrapInt pIndex){
		Random rnd = new Random();
		int num=0, mov;
		
		//Contar cuantos pokemons disponibles (contando el que esta en uso)
		for (int i=0; i<num_pokes; i++)
			if (Poke[i].HP>0)
				num++;
		
		//Si tiene mas de un pokemon disponible
		if (num>1){
			//Elegir Movimiento aleatorio de 0 a 4
			mov = rnd.nextInt(5);
			//Si el movimiento elegido es cambiar de pokemon
			if (mov==4){
				while (true){
					//Elige un pokemon aleatoriamente (siempre y cuando tenga HP>0)
					pIndex.Num = rnd.nextInt(num_pokes);
					if (Poke[pIndex.Num].HP>0 && Poke[pIndex.Num]!=this)
						break;
				}
			}
		}
		else
			//Elegir Movimiento aleatorio de 0 a 3	
			mov = rnd.nextInt(4);					
		
		return mov;		
	}
	
	public int heuristica1(Pokemon P1, Pokemon P2, Pokemon[] Pokemones_persona,Pokemon[] Pokemones_pc,int num_pokemones){//P1 es persona, P2 es computadora
		//comenzaremos con max para la computadora
		NodoMiniMax[] grafo= new NodoMiniMax[(4+num_pokemones)*(4+num_pokemones)];
		/*for(int i=0;i<(4+num_pokemones)*(4+num_pokemones);++i){
			grafo[i].opcion_escogido=new int;
			
		}*/
		for(int i=0;i<(4+num_pokemones)*(4+num_pokemones);i++){
			grafo[i]=new NodoMiniMax();
		}
		for(int i=0;i<4+num_pokemones;++i){
			if(i<4){				
				grafo[i].opcion_escogido=i;
				grafo[i].valor_heuristico=Math.abs(P1.HP-P2.HP-Integer.parseInt(P2.moves[i][1]) );
			}
			else{
				grafo[i].opcion_escogido=i;
				if(Pokemones_pc[i-4].HP>0){
					grafo[i].valor_heuristico=Math.abs(P1.HP-Pokemones_pc[i-4].HP);
					if(Pokemones_pc[i-4]==this)grafo[i].valor_heuristico=-999;
				}
				else grafo[i].valor_heuristico=0;
			}
		}
		for(int i=0;i<4+num_pokemones;++i){
			for(int x=0;x<4+num_pokemones;++x){
				if(x<4){
					grafo[i+x+4+num_pokemones].opcion_escogido=i;
					grafo[i+x+4+num_pokemones].valor_heuristico=Math.abs(grafo[i].valor_heuristico-Integer.parseInt(P1.moves[x][1]));
				}
				else{
					grafo[i+x+4+num_pokemones].opcion_escogido=i;
					if(Pokemones_persona[x-4].HP>0){
						grafo[i+x+4+num_pokemones].valor_heuristico=Math.abs(P1.HP-Pokemones_persona[x-4].HP);
						if(Pokemones_persona[x-4]==P1)grafo[i].valor_heuristico=9999999;
					}
					else grafo[i+x+4+num_pokemones].valor_heuristico=0;
				}
			}
		}
		for(int i=0;i<4+num_pokemones;++i){
			int min=99999;
			for(int x=0;x<4+num_pokemones;++x){					
				if(grafo[i+x+4+num_pokemones].valor_heuristico<min){
					min=grafo[i+x+4+num_pokemones].valor_heuristico;
				}
			}
			grafo[i].valor_final=min;
		}
		int mov=0,max=0;
		for(int i=0;i<4+num_pokemones;++i){					
			if(grafo[i].valor_heuristico>max){
				max=grafo[i].valor_heuristico;
				mov=grafo[i].opcion_escogido;
			}
		}
		
		return mov;
}
	
}
