package controller;

import enums.StatusVenda;
import model.*;
import repository.DadosLoja;

import java.util.List;

public class VendaController {
    private final DadosLoja dados;

    public VendaController(DadosLoja dados) {
        this.dados = dados;
    }

    public Venda realizarVenda(
            int id,
            Cliente cliente,
            Funcionario funcionario,
            List<ItemVenda> itens) {

        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException(
                    "A venda precisa ter pelo menos um item."
            );
        }

        for (ItemVenda item : itens) {
            Produto produto = item.getProduto();

            if (dados.getProdutos()
                    .buscarPorId(produto.getId())
                    .orElse(null) != produto) {
                throw new IllegalArgumentException(
                        "O produto não está cadastrado nesta loja."
                );
            }
        }

        for (Produto produto : dados.getProdutos().listarTodos()) {
            int quantidadeNecessaria = itens.stream()
                    .filter(item -> item.getProduto() == produto)
                    .mapToInt(ItemVenda::getQuantidade)
                    .sum();

            if (quantidadeNecessaria > produto.getEstoque()) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para: " + produto.getNome()
                );
            }
        }

        if (dados.getVendas().buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe uma venda com esse ID."
            );
        }

        Venda venda = new Venda(id, cliente, funcionario);

        for (ItemVenda item : itens) {
            venda.adicionarItem(item);
        }

        for (ItemVenda item : itens) {
            item.getProduto().retirarEstoque(
                    item.getQuantidade()
            );
        }

        dados.getVendas().adicionar(venda);

        return venda;
    }

    public void devolverVenda(int idVenda) {
        Venda venda = dados.getVendas()
                .buscarPorId(idVenda)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Venda não encontrada."
                        )
                );

        if (venda.getStatus() == StatusVenda.DEVOLVIDA) {
            throw new IllegalStateException(
                    "Esta venda já foi devolvida."
            );
        }

        for (ItemVenda item : venda.getItens()) {
            item.getProduto().adicionarEstoque(
                    item.getQuantidade()
            );
        }

        venda.marcarComoDevolvida();
    }

    public List<Venda> listarTodas() {
        return dados.getVendas().listarTodos();
    }

    public Venda buscarPorId(int id) {
        return dados.getVendas()
                .buscarPorId(id)
                .orElse(null);
    }
}
