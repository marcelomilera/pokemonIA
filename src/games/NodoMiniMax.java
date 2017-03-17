package games;

public class NodoMiniMax {
	public NodoMiniMax() {
		super();
		this.valor_heuristico = 0;
		this.opcion_escogido = 0;
		this.valor_final = 0;
	}
	public int valor_heuristico;
	public int getValor_heuristico() {
		return valor_heuristico;
	}
	public void setValor_heuristico(int valor_heuristico) {
		this.valor_heuristico = valor_heuristico;
	}
	public int getOpcion_escogido() {
		return opcion_escogido;
	}
	public void setOpcion_escogido(int opcion_escogido) {
		this.opcion_escogido = opcion_escogido;
	}
	public int getValor_final() {
		return valor_final;
	}
	public void setValor_final(int valor_final) {
		this.valor_final = valor_final;
	}
	public int opcion_escogido;
	public int valor_final=0;
	
	
}
