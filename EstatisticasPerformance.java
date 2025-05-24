class EstatisticasPerformance {
    public long tempoInsercao;
    public long tempoBusca;
    public int colisoes;
    public int[] distribuicao;
    public String nomeHashFunction;
    
    public EstatisticasPerformance(String nome) {
        this.nomeHashFunction = nome;
    }
}