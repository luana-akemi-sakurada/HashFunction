abstract class TabelaHash {
    protected String[] tabela;
    protected int capacidade;
    protected int tamanho;
    protected int colisoes;
    protected int[] distribuicao;
    
    public TabelaHash(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new String[capacidade];
        this.tamanho = 0;
        this.colisoes = 0;
        this.distribuicao = new int[capacidade];
    }
    
    // Método abstrato que será implementado pelas classes filhas
    protected abstract int funcaoHash(String chave);
    
    // Método para inserir uma chave na tabela
    public void inserir(String chave) {
        int indice = funcaoHash(chave);
        int indiceOriginal = indice;
        
        // Tratamento de colisão por sondagem linear
        while (tabela[indice] != null) {
            if (tabela[indice].equals(chave)) {
                return; // Chave já existe
            }
            colisoes++;
            indice = (indice + 1) % capacidade;
            
            // Evitar loop infinito se a tabela estiver cheia
            if (indice == indiceOriginal) {
                throw new RuntimeException("Tabela hash cheia!");
            }
        }
        
        tabela[indice] = chave;
        distribuicao[indice]++;
        tamanho++;
    }
    
    // Método para buscar uma chave na tabela
    public boolean buscar(String chave) {
        int indice = funcaoHash(chave);
        int indiceOriginal = indice;
        
        while (tabela[indice] != null) {
            if (tabela[indice].equals(chave)) {
                return true;
            }
            indice = (indice + 1) % capacidade;
            
            if (indice == indiceOriginal) {
                break;
            }
        }
        return false;
    }
    
    // Getters
    public int getColisoes() { return colisoes; }
    public int getTamanho() { return tamanho; }
    public int[] getDistribuicao() { return distribuicao; }
    public String[] getTabela() { return tabela; }
}
