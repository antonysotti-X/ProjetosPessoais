package view;

import controller.ProdutoController;
import enums.Genero;
import enums.Plataforma;
import model.Acessorio;
import model.Console;
import model.Jogo;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaProdutos extends JFrame {
    private final ProdutoController controller;

    private final JTextField txtId = new JTextField();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtPreco = new JTextField();
    private final JTextField txtEstoque = new JTextField();

    private final JTextField txtExtra1 = new JTextField();
    private final JTextField txtExtra2 = new JTextField();
    private final JTextField txtExtra3 = new JTextField();

    private final JComboBox<String> cmbCategoria =
            new JComboBox<>(new String[]{
                    "Jogo", "Console", "Acessório"
            });

    private final JComboBox<Plataforma> cmbPlataforma =
            new JComboBox<>(Plataforma.values());

    private final JComboBox<Genero> cmbGenero =
            new JComboBox<>(Genero.values());

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new Object[]{"ID", "Nome", "Categoria", "Preço", "Estoque"},
                    0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    public TelaProdutos(
            JFrame pai,
            ProdutoController controller) {

        this.controller = controller;

        setTitle("Cadastro de Produtos");
        setSize(850, 600);
        setLocationRelativeTo(pai);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        criarInterface();
        atualizarTabela();
    }

    private void criarInterface() {
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Dados do produto"
        ));

        adicionarCampo(formulario, "ID:", txtId);
        adicionarCampo(formulario, "Nome:", txtNome);
        adicionarCampo(formulario, "Preço:", txtPreco);
        adicionarCampo(formulario, "Estoque:", txtEstoque);
        adicionarCampo(formulario, "Categoria:", cmbCategoria);

        adicionarCampo(formulario, "Plataforma (jogo):", cmbPlataforma);
        adicionarCampo(formulario, "Gênero (jogo):", cmbGenero);
        adicionarCampo(formulario, "Dado extra 1:", txtExtra1);
        adicionarCampo(formulario, "Dado extra 2:", txtExtra2);
        adicionarCampo(formulario, "Dado extra 3:", txtExtra3);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnLimpar = new JButton("Limpar campos");

        btnCadastrar.addActionListener(e -> cadastrarProduto());
        btnLimpar.addActionListener(e -> limparCampos());

        JPanel botoes = new JPanel();
        botoes.add(btnCadastrar);
        botoes.add(btnLimpar);

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(formulario, BorderLayout.CENTER);
        topo.add(botoes, BorderLayout.SOUTH);

        JTable tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        setLayout(new BorderLayout(10, 10));
        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void adicionarCampo(
            JPanel painel,
            String texto,
            Component campo) {

        painel.add(new JLabel(texto));
        painel.add(campo);
    }

    private void cadastrarProduto() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nome = txtNome.getText().trim();
            double preco = Double.parseDouble(
                    txtPreco.getText().trim().replace(",", ".")
            );
            int estoque = Integer.parseInt(
                    txtEstoque.getText().trim()
            );

            String categoria =
                    (String) cmbCategoria.getSelectedItem();

            Produto produto;

            switch (categoria) {
                case "Jogo":
                    int idade = Integer.parseInt(
                            txtExtra1.getText().trim()
                    );

                    produto = new Jogo(
                            id, nome, preco, estoque,
                            (Plataforma) cmbPlataforma.getSelectedItem(),
                            (Genero) cmbGenero.getSelectedItem(),
                            idade
                    );
                    break;

                case "Console":
                    produto = new Console(
                            id, nome, preco, estoque,
                            txtExtra1.getText().trim(),
                            txtExtra2.getText().trim(),
                            Integer.parseInt(txtExtra3.getText().trim())
                    );
                    break;

                case "Acessório":
                    produto = new Acessorio(
                            id, nome, preco, estoque,
                            txtExtra1.getText().trim(),
                            txtExtra2.getText().trim()
                    );
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Categoria inválida."
                    );
            }

            controller.cadastrar(produto);
            atualizarTabela();
            limparCampos();

            JOptionPane.showMessageDialog(
                    this,
                    "Produto cadastrado com sucesso!"
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Verifique os campos numéricos: ID, preço, estoque e dados extras.",
                    "Dados inválidos",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Não foi possível cadastrar",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);

        for (Produto p : controller.listarTodos()) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getCategoria(),
                    String.format("%.2f", p.getPreco()),
                    p.getEstoque()
            });
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        txtExtra1.setText("");
        txtExtra2.setText("");
        txtExtra3.setText("");
    }
}
