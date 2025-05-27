//ComparadorTabelasHash.java
import java.io.*;
import java.util.*;

public class ComparadorTabelasHash {
    private static final int CAPACIDADE_TABELA = 32;
    private static final String ARQUIVO_NOMES = "female_names.txt";
    
    public static void main(String[] args) {
        try {
            // Lê os nomes do arquivo
            List<String> nomes = lerNomesDoArquivo();
            
            if (nomes.isEmpty()) {
                System.err.println("ERRO: Arquivo female_names.txt não encontrado!");
                System.err.println("Por favor, coloque o arquivo no diretório do programa.");
                return; 
            }
            
            System.out.println("=== COMPARAÇÃO DE TABELAS HASH ===");
            System.out.println("Número de nomes carregados: " + nomes.size());
            System.out.println("Capacidade da tabela: " + CAPACIDADE_TABELA);
            System.out.println();
            
            // Testa a primeira função hash
            Hash1 tabela1 = new Hash1(CAPACIDADE_TABELA);
            EstatisticasPerformance stats1 = testarTabelaHash(tabela1, nomes, tabela1.getNome());
            
            // Testa a segunda função hash
            Hash2 tabela2 = new Hash2(CAPACIDADE_TABELA);
            EstatisticasPerformance stats2 = testarTabelaHash(tabela2, nomes, tabela2.getNome());
            
            // Gera relatório comparativo
            gerarRelatorioComparativo(stats1, stats2);
            
        } catch (Exception e) {
            System.err.println("Erro durante execução: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<String> lerNomesDoArquivo() {
        List<String> nomes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_NOMES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty()) {
                    nomes.add(linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Aviso: Não foi possível ler o arquivo " + ARQUIVO_NOMES);
        }
        return nomes;
    }
    
    private static EstatisticasPerformance testarTabelaHash(TabelaHash tabela, List<String> nomes, String nomeHash) {
        EstatisticasPerformance stats = new EstatisticasPerformance(nomeHash);
        
        System.out.println("--- Testando: " + nomeHash + " ---");
        
        // Teste de inserção
        long inicioInsercao = System.nanoTime();
        int nomesInseridos = 0;
        
        for (String nome : nomes) {
            tabela.inserir(nome);
            nomesInseridos++;
        }
        
        long fimInsercao = System.nanoTime();
        stats.tempoInsercao = fimInsercao - inicioInsercao;
        stats.colisoes = tabela.getColisoes();
        stats.distribuicao = tabela.getDistribuicao().clone();
        
        System.out.println("Nomes inseridos: " + nomesInseridos);
        System.out.println("Colisões durante inserção: " + stats.colisoes);
        System.out.println("Tempo de inserção: " + (stats.tempoInsercao / 1_000_000.0) + " ms");
        
        // Teste de busca
        List<String> nomesParaBuscar = nomes.subList(0, Math.min(nomesInseridos, nomes.size()));
        
        long inicioBusca = System.nanoTime();
        int encontrados = 0;
        
        for (String nome : nomesParaBuscar) {
            if (tabela.buscar(nome)) {
                encontrados++;
            }
        }
        
        long fimBusca = System.nanoTime();
        stats.tempoBusca = fimBusca - inicioBusca;
        
        System.out.println("Buscas realizadas: " + nomesParaBuscar.size());
        System.out.println("Nomes encontrados: " + encontrados);
        System.out.println("Tempo de busca: " + (stats.tempoBusca / 1_000_000.0) + " ms");
        System.out.println();
        
        return stats;
    }
    
    private static void gerarRelatorioComparativo(EstatisticasPerformance stats1, EstatisticasPerformance stats2) {
        System.out.println("=== RELATÓRIO COMPARATIVO ===");
        System.out.println();
        
        // Comparação de colisões
        System.out.println("1. NÚMERO DE COLISÕES:");
        System.out.println("   " + stats1.nomeHashFunction + ": " + stats1.colisoes + " colisões");
        System.out.println("   " + stats2.nomeHashFunction + ": " + stats2.colisoes + " colisões");
        
        if (stats1.colisoes < stats2.colisoes) {
            System.out.println("   → " + stats1.nomeHashFunction + " teve MENOS colisões");
        } else if (stats2.colisoes < stats1.colisoes) {
            System.out.println("   → " + stats2.nomeHashFunction + " teve MENOS colisões");
        } else {
            System.out.println("   → Ambas tiveram o mesmo número de colisões");
        }
        System.out.println();
        
        // Comparação de tempos
        System.out.println("2. TEMPOS DE EXECUÇÃO:");
        System.out.println("   Inserção:");
        System.out.println("     " + stats1.nomeHashFunction + ": " + (stats1.tempoInsercao / 1_000_000.0) + " ms");
        System.out.println("     " + stats2.nomeHashFunction + ": " + (stats2.tempoInsercao / 1_000_000.0) + " ms");
        
        System.out.println("   Busca:");
        System.out.println("     " + stats1.nomeHashFunction + ": " + (stats1.tempoBusca / 1_000_000.0) + " ms");
        System.out.println("     " + stats2.nomeHashFunction + ": " + (stats2.tempoBusca / 1_000_000.0) + " ms");
        System.out.println();
        
        // Distribuição das chaves
        System.out.println("3. DISTRIBUIÇÃO DAS CHAVES:");
        System.out.println("   " + stats1.nomeHashFunction + ":");
        imprimirDistribuicao(stats1.distribuicao);
        System.out.println();
        
        System.out.println("   " + stats2.nomeHashFunction + ":");
        imprimirDistribuicao(stats2.distribuicao);
        System.out.println();
        
        // Análise de clusterização
        System.out.println("4. ANÁLISE DE CLUSTERIZAÇÃO:");
        analisarClusterizacao(stats1);
        analisarClusterizacao(stats2);
        
        // Conclusão
        System.out.println("5. CONCLUSÃO:");
        if (stats1.colisoes < stats2.colisoes) {
            System.out.println("   A função " + stats1.nomeHashFunction + " apresentou melhor distribuição");
            System.out.println("   com menor número de colisões (" + stats1.colisoes + " vs " + stats2.colisoes + ").");
        } else if (stats2.colisoes < stats1.colisoes) {
            System.out.println("   A função " + stats2.nomeHashFunction + " apresentou melhor distribuição");
            System.out.println("   com menor número de colisões (" + stats2.colisoes + " vs " + stats1.colisoes + ").");
        } else {
            System.out.println("   Ambas as funções hash tiveram performance similar em termos de colisões.");
        }
    }
    
    private static void imprimirDistribuicao(int[] distribuicao) {
        for (int i = 0; i < distribuicao.length; i++) {
            if (distribuicao[i] > 0) {
                System.out.print("     Posição " + String.format("%2d", i) + ": " + distribuicao[i] + " chave(s)");
                
                // Visualização gráfica simples
                System.out.print(" [");
                for (int j = 0; j < distribuicao[i] && j < 10; j++) {
                    System.out.print("*");
                }
                if (distribuicao[i] > 10) {
                    System.out.print("...");
                }
                System.out.println("]");
            }
        }
    }
    
    private static void analisarClusterizacao(EstatisticasPerformance stats) {
        int posicaoOcupadas = 0;
        int maiorCluster = 0;
        int clusterAtual = 0;
        
        for (int i = 0; i < stats.distribuicao.length; i++) {
            if (stats.distribuicao[i] > 0) {
                posicaoOcupadas++;
                clusterAtual++;
                maiorCluster = Math.max(maiorCluster, clusterAtual);
            } else {
                clusterAtual = 0;
            }
        }
        
        double fatorCarga = (double) posicaoOcupadas / stats.distribuicao.length;
        
        System.out.println("   " + stats.nomeHashFunction + ":");
        System.out.println("     Posições ocupadas: " + posicaoOcupadas + "/" + stats.distribuicao.length);
        System.out.println("     Fator de carga: " + String.format("%.2f", fatorCarga));
        System.out.println("     Maior cluster consecutivo: " + maiorCluster);
    }
}