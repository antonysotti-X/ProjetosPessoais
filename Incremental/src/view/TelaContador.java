package view;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sun.jvm.hotspot.utilities.Unsigned5.SetPosition;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TelaContador extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Integer contador;
	JLabel MostrarClicks;
	private JTextField ValorInicialCampo;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaContador frame = new TelaContador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public TelaContador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(new Color(0, 0, 0));
		contentPane.setLayout(null);
		
		ValorInicialCampo = new JTextField("0");
		contentPane.add(ValorInicialCampo);
		ValorInicialCampo.setColumns(10);
		ValorInicialCampo.setBounds(200, 66, 100, 25);
		
		
		
		this.MostrarClicks = new JLabel("Clicks: 0");
		MostrarClicks.setForeground(new Color(255, 0, 0));
		MostrarClicks.setFont(new Font("Papyrus", Font.PLAIN, 50));
		contentPane.add(MostrarClicks);
		MostrarClicks.setBounds(133, 158, 262, 108);
		
		JButton botao = new JButton("Click");
		botao.setForeground(new Color(255, 0, 0));
		botao.setFont(new Font("Papyrus", Font.PLAIN, 25));
		contentPane.add(botao);
		botao.setBackground(Color.black);
		botao.addActionListener(e->IncrementarContador());
		botao.setBounds(200, 315, 100, 100);
		
		this.contador=0;
	}

	
	private void IncrementarContador() {
		
		if(ValorInicialCampo.isEnabled()) {
			Integer ValorInicial = Integer.parseInt(ValorInicialCampo.getText());
			contador = ValorInicial;
			ValorInicialCampo.setEnabled(false);
		}
		contador++;
		MostrarClicks.setText("Clicks: "+contador);
		
	}
}
