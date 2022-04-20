package br.com.ryan.gui;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.com.ryan.model.Funcionarios;

public class GUI_Funcionario extends JFrame {
    public GUI_Funcionario() {
        initComponents();
    }

    private Connection con;
    private Statement stmt;
    private String sql;

    public void Conectar() {
        final String serverName = "localhost:3307/empresa?useTimezone=true&serverTimezone=UTC&useSSL=false";
        final String username = "root";
        final String password = "usbw";
        final String url = "jdbc:mysql://" + serverName;
        
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
        }  catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar o Banco de Dados: " + e.getMessage());
        }
    }

    public void setDados() {
        String nome;
        String departamento;
        BigDecimal salario;

        nome = this.jTextField1.getText();
        salario = new BigDecimal(this.jTextField2.getText());
        departamento = this.jTextField3.getText();

        try {
            sql = "insert into Funcionarios (nome_funcionario,salario_funcionario,departamento_funcionario) values "
                    + "('" + nome + "'," + salario + ", '" + departamento + "')";
            stmt.executeUpdate(sql);
            
            JOptionPane.showMessageDialog(null, "Funcionário cadastrado!");
            limpar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar com o BD: " + e.getMessage());
        }
    }

    public void getDados() {
        ArrayList<String> funcionarios = new ArrayList<String>();
        ResultSet rs;

        try {
            sql = "select * from Funcionarios";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Funcionarios func = new Funcionarios();
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome_funcionario"));
                func.setSalario(new BigDecimal(rs.getString("salario_funcionario")));
                func.setDepartamento(rs.getString("departamento_funcionario"));
                
                funcionarios.add("Nome: " + func.getNome() + " - Saáario: R$" + func.getSalario() + " - Departamento: " + func.getDepartamento());
            }
            
            funcionarios.forEach(System.out::println);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar com o BD: " + e.getMessage());
        }
    }

    public void AlteraDados() {
        ResultSet rs;
        String nome, departamento, op;
        BigDecimal salario;
        long cod;

        cod = Integer.parseInt(this.jTextField4.getText());

        try {
            sql = "SELECT * FROM Funcionarios WHERE id"
                    + "='" + cod + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nome = rs.getString("nome_funcionario");
                departamento = rs.getString("departamento_funcionario");
                salario = new BigDecimal("salario_funcionario");
                JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalário: R$" + salario + "\nDepartamento: " + departamento);
                
                try {
                    op = JOptionPane.showInputDialog("Deseja realmente excluir S/N: ");
                    if (op.equals("s") || (op.equals("S"))) {
                        sql = "UPDATE Funcionarios SET nome_funcionario=?,salario_funcionario=?,departamento_funcionario=? WHERE id='" + cod + "'";
                        PreparedStatement pstm;
                        
                        pstm = con.prepareStatement(sql);
                        pstm.setString(1, this.jTextField1.getText());
                        pstm.setString(2, this.jTextField2.getText());
                        pstm.setString(3, this.jTextField3.getText());
                        pstm.execute();
                        JOptionPane.showMessageDialog(null, "Registro Alterado...");
                    } else {
                        JOptionPane.showMessageDialog(null, "Registro não alterado...");
                    }
                    
                    limpar();
                } catch (SQLException e) {
                    System.out.println("Erro ao executar o comando SQL!\n" + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.getMessage());
        }
    }

	private void limpar() {
		this.jTextField1.setText(null);
		this.jTextField2.setText(null);
		this.jTextField3.setText(null);
		this.jTextField4.setText(null);
	}

    public void ExcluirDados() {
        ResultSet rs;
        String nome, departamento, op;
        int cod;
        float salario;

        cod = Integer.parseInt(this.jTextField4.getText());

        try {
            sql = "SELECT * FROM Funcionarios WHERE id='" + cod + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nome = rs.getString("nome_funcionario");
                departamento = rs.getString("departamento_funcionario");
                salario = rs.getFloat("salario_funcionario");
                
                try {
                    JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalário: R$" + salario + "\nDepartamento: " + departamento);
                    op = JOptionPane.showInputDialog("Deseja realmente excluir S/N: ");
                    if (op.equals("s") || (op.equals("S"))) {
                        sql = "DELETE FROM Funcionarios WHERE id='" + cod + "'";
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Registro excluido...");
                        limpar();
                    } else {
                        limpar();
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao executar o comando SQL:" + e.getMessage());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.getMessage());
        }
    }

    public void ConsultaDados() {
        ResultSet rs;
        String nome;
        String departamento;
        float salario;
        int cod;

        cod = Integer.parseInt(this.jTextField4.getText());

        try {
            sql = "SELECT * FROM Funcionarios WHERE id='" + cod + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nome = rs.getString("nome_funcionario");
                departamento = rs.getString("departamento_funcionario");
                salario = rs.getFloat("salario_funcionario");
                JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalário: R$" + salario + "\nDepartamento: " + departamento);
                limpar();
            } else {
                JOptionPane.showMessageDialog(null, "Registro não encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.getMessage());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 24)); // NOI18N
        jLabel1.setText("FUNCIONÁRIO");

        jLabel2.setText("Nome:");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        
        jLabel3.setText("Salário::");
        
        jLabel4.setText("Departamento:");
        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jButton1.setText("Sair");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Limpar");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Cadastrar");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Exibir");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setText("Alterar");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setText("Excluir");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jLabel5.setText("Codigo (id):");
        jTextField4.addActionListener(this::jTextField4ActionPerformed);

        jButton7.setText("Consultar");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabel1)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton7))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 10, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton7))))
        );

        pack();
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) { }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(null, "Saindo...");
        System.exit(0);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        limpar();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        this.Conectar();
        this.setDados();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        this.Conectar();
        this.getDados();
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) { }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        this.Conectar();
        this.AlteraDados();
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        this.Conectar();
        this.ExcluirDados();
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        this.Conectar();
        this.ConsultaDados();
    }

	private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {
	}

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Funcionario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> new GUI_Funcionario().setVisible(true));
    }
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
}