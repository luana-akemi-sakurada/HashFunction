abstract class TabelaHash {
    protected String[] tabela;
    protected int capacidade;
    protected int tamanho;
    protected int colisoes;
    protected int[] distribuicao;
    protected static final double FATOR_CARGA_MAXIMO = 0.75; // 75%
    
    public TabelaHash(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new String[capacidade];
        this.tamanho = 0;
        this.colisoes = 0;
        this.distribuicao = new int[capacidade];
    }
    
    // Método abstrato que será implementado pelas classes filhas
    protected abstract int funcaoHash(String chave);
    
    // Método para calcular o fator de carga atual
    private double getFatorCarga() {
        return (double) tamanho / capacidade;
    }
    
    // Método para redimensionar a tabela
    private void redimensionar() {
        System.out.println("Redimensionando tabela de " + capacidade + " para " + (capacidade * 2));
        
        // Salva os dados atuais
        String[] tabelaAntiga = tabela;
        int capacidadeAntiga = capacidade;
        
        // Duplica a capacidade
        capacidade *= 2;
        tabela = new String[capacidade];
        distribuicao = new int[capacidade];
        
        // Reset contadores
        tamanho = 0;
        int colisoesAntes = colisoes;
        colisoes = 0;
        
        // Rehashing: reinsere todos os elementos
        for (int i = 0; i < capacidadeAntiga; i++) {
            if (tabelaAntiga[i] != null) {
                inserirSemVerificarRedimensionamento(tabelaAntiga[i]);
            }
        }
        
        System.out.println("Redimensionamento concluído. Elementos reinseridos: " + tamanho);
        System.out.println("Colisões antes: " + colisoesAntes + ", após rehashing: " + colisoes);
    }
    
    // Método de inserção sem verificação de redimensionamento (usado no rehashing)
    private void inserirSemVerificarRedimensionamento(String chave) {
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
                throw new RuntimeException("Tabela hash cheia mesmo após redimensionamento!");
            }
        }
        
        tabela[indice] = chave;
        distribuicao[indice]++;
        tamanho++;
    }
    
    // Método para inserir uma chave na tabela (com verificação de redimensionamento)
    public void inserir(String chave) {
        // Verifica se precisa redimensionar antes de inserir
        if (getFatorCarga() >= FATOR_CARGA_MAXIMO) {
            redimensionar();
        }
        
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
                // Se chegou aqui, tenta redimensionar uma vez mais
                redimensionar();
                inserir(chave); // Tenta inserir novamente após redimensionamento
                return;
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
    
    // Método para obter informações sobre o redimensionamento
    public void imprimirEstatisticas() {
        System.out.println("Capacidade atual: " + capacidade);
        System.out.println("Elementos inseridos: " + tamanho);
        System.out.println("Fator de carga: " + String.format("%.2f", getFatorCarga()));
        System.out.println("Total de colisões: " + colisoes);
    }
    
    // Getters
    public int getColisoes() { return colisoes; }
    public int getTamanho() { return tamanho; }
    public int getCapacidade() { return capacidade; }
    public double getFatorCargaAtual() { return getFatorCarga(); }
    public int[] getDistribuicao() { return distribuicao; }
    public String[] getTabela() { return tabela; }
}