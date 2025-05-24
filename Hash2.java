class Hash2 extends TabelaHash {
    public Hash2(int capacidade) {
        super(capacidade);
    }
    
    @Override
    protected int funcaoHash(String chave) {
        int hash = 5381;
        for (int i = 0; i < chave.length(); i++) {
            hash = ((hash << 5) + hash) + chave.charAt(i);
        }
        return Math.abs(hash) % capacidade;
    }
    
    public String getNome() {
        return "Hash2 - Hash Polinomial (djb2)";
    }
}