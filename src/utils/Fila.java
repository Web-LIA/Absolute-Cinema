package utils;

public class Fila {
    private double[][] fila;
    private int tamanho;
    
    public Fila(int tamanho) {
        this.tamanho = tamanho;
        this.fila = new double[tamanho][4]; // [posição][0] = x, [posição][1] = y
        for (int i = 0; i < tamanho; i++) {
            fila[i][0] = 0; // Inicializa x
            fila[i][1] = 0; // Inicializa y
            fila[i][2] = 0; // Inicializa id da pessoa na fila
            fila[i][3] = 0; // Inicializa direcao da pessoa  
        }
    }
    public double[][] getFila() {
        return fila;
    }
    public int getTamanho() {
        return tamanho;
    }
    public void setFila(double[][] fila) {
        this.fila = fila;
    }
    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
    public void clear() {
        for (int i = 0; i < tamanho; i++) {
            fila[i][3] = 0;
        }
    }
    public void remove(int x, int y) {
        for (int i = 0; i < tamanho; i++) {
            if (fila[i][0] == x && fila[i][1] == y) {
                fila[i][0] = 0;
                fila[i][1] = 0;
                break;
            }
        }
    }
    public void addXY(double x, double y , double direcao ) {
        for (int i = 0; i < tamanho; i++) {
            if (fila[i][0] == 0 && fila[i][1] == 0) {
                fila[i][0] = x;
                fila[i][1] = y;
                fila[i][2] = 0; // id da pessoa na fila
                fila[i][3] = direcao;
                break;
            }
        }
    }
    public void addPerson(int posicion, int id) {
        for (int i = 0; i < tamanho; i++) {
            if(i == posicion) {
                fila[i][2] = id; // id da pessoa na fila
                break;
            }
        }
    }
    public int positionEmpty() {
        if(fila == null || tamanho <= 0) {
            return -2; // Fila inválida
        }
        if (fila.length < tamanho) {
            return -2; // Fila inválida
        }
        
        for (int i = 0; i < tamanho; i++) {
            if(fila[i][0] == 0 && fila[i][1] == 0) {
                return -3; // fila sem coordenadas
            }
            if (fila[i][2] == 0 ) {
                return i; // Há espaço na fila
            }
        }
        return -1; // Fila cheia
    }
    public void removePerson(int posicao) {
        if (posicao >= 0 && posicao < tamanho) {
            fila[posicao][2] = 0; // Libera a cadeira
        }
    }
    public int positionPerson(int id) {
        for (int i = 0; i < tamanho; i++) {
            if (fila[i][2] == id) {
                return i; // Retorna a posição da pessoa na fila
            }
        }
        return -1; // Pessoa não encontrada
    }
    public int getDirecaoPosicao(int posicao) {
        if (posicao >= 0 && posicao < tamanho) {
            return (int) fila[posicao][3]; // Retorna a direção da pessoa na fila
        }
        return -1; // Posição inválida
    }
    public int getDirecaoPessoa(int id) {
        for (int i = 0; i < tamanho; i++) {
            if (fila[i][2] == id) {
                return (int) fila[i][3]; // Retorna a direção da pessoa na fila
            }
        }
        return -1; // Pessoa não encontrada
    }
    public void setDirecaoPessoa(int id, int direcao) {
        for (int i = 0; i < tamanho; i++) {
            if (fila[i][2] == id) {
                fila[i][3] = direcao; // Define a direção da pessoa na fila
                break;
            }
        }
    }
    public void setDirecaoPosicao(int posicao, int direcao) {
        if (posicao >= 0 && posicao < tamanho) {
            fila[posicao][3] = direcao; // Define a direção da pessoa na fila
        }
    }
    public void showStatus() {
        for (int i = 0; i < tamanho; i++) {
            System.out.println("Posição: " + i + " | X: " + fila[i][0] + " | Y: " + fila[i][1] + " | ID: " + fila[i][2] + " | Direção: " + fila[i][3]);
        }
    }
    public double[] getPerson(int id){
        int pos =this.positionPerson(id);
        if(pos == -1){
            double[] error = {-1,-1,-1};
            return error;
        }
        double[] coord = {this.fila[pos][0],this.fila[pos][1],this.fila[pos][2]};
        return coord;
    }
}
