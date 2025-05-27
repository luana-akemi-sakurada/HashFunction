class Hash1 extends TabelaHash {
    public Hash1(int capacidade) {
        super(capacidade);
    }
    
    @Override
    protected int funcaoHash(String chave) {
        int hash = 0;
        for (int i = 0; i < chave.length(); i++) {
            hash += chave.charAt(i);
        }
        return Math.abs(hash) % capacidade;
    }
    
    public String getNome() {
        return "Hash1";
    }
}