import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GUI_Funcionario extends javax.swing.JFrame 
{
    public GUI_Funcionario() {
        initComponents();
    }

    private Connection con;
    private Statement stmt;
    String sql;

    public void Conectar() {
        final String serverName = "localhost:3307/empresa?useTimezone=true&serverTimezone=UTC&useSSL=false";
        final String username = "root";
        final String password = "usbw";
        final String url = "jdbc:mysql://" + serverName;
        
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar o Banco de Dados: " + e.toString());
        }
    }

    public void setDados() {
        String nome;
        String departamento;
        float salario;

        nome = this.jTextField1.getText();
        salario = Float.parseFloat(this.jTextField2.getText());
        departamento = this.jTextField3.getText();

        try {
            sql = "insert into Funcionarios (nome_funcionario,salario_funcionario,departamento_funcionario) values "
                    + "('" + nome + "'," + salario + ", '" + departamento + "')";
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "FuncionÃ¡rio cadastrado!");
            this.jTextField1.setText(null);
            this.jTextField2.setText(null);
            this.jTextField3.setText(null);
            this.jTextField4.setText(null);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar com o BD: " + e.toString());
        }
    }

    public void getDados() {
        ArrayList<String> Funcionarios = new ArrayList();
        ResultSet rs;
        String msg;

        try {
            sql = "select * from Funcionarios";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Funcionarios func = new Funcionarios();
                func.setId(rs.getInt("id"));
                func.setNome(rs.getString("nome_funcionario"));
                func.setSalario(Float.parseFloat(rs.getString("salario_funcionario")));
                func.setDepartamento(rs.getString("departamento_funcionario"));
                msg = "Nome: " + func.getNome() + " - SalÃ¡rio: R$" + func.getSalario() + " - Departamento: " + func.getDepartamento();
                Funcionarios.add(msg);
            }
            for (int i = 0; i < Funcionarios.size(); i++) {
                System.out.println(Funcionarios.get(i));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao executar com o BD: " + e.toString());
        }
    }

    public void AlteraDados() {
        ResultSet rs;
        String nome, departamento, op;
        float salario;
        int cod;

        cod = Integer.parseInt(this.jTextField4.getText());

        try {
            sql = "SELECT * FROM Funcionarios WHERE id"
                    + "='" + cod + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                nome = rs.getString("nome_funcionario");
                departamento = rs.getString("departamento_funcionario");
                salario = rs.getFloat("salario_funcionario");
                JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalÃ¡rio: R$" + salario + "\nDepartamento: " + departamento);
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
                        JOptionPane.showMessageDialog(null, "Registro nÃ£o alterado...");
                    }
                    this.jTextField1.setText(null);
                    this.jTextField2.setText(null);
                    this.jTextField3.setText(null);
                    this.jTextField4.setText(null);
                } catch (SQLException e) {
                    System.out.println("Erro ao executar o comando SQL:" + e.toString());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Registro nÃ£o encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.toString());
        }
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
                    JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalÃ¡rio: R$" + salario + "\nDepartamento: " + departamento);
                    op = JOptionPane.showInputDialog("Deseja realmente excluir S/N: ");
                    if (op.equals("s") || (op.equals("S"))) {
                        sql = "DELETE FROM Funcionarios WHERE id='" + cod + "'";
                        stmt.executeUpdate(sql);
                        JOptionPane.showMessageDialog(null, "Registro excluido...");
                        this.jTextField1.setText(null);
                        this.jTextField2.setText(null);
                        this.jTextField3.setText(null);
                        this.jTextField4.setText(null);
                    } else {
                        this.jTextField1.setText(null);
                        this.jTextField2.setText(null);
                        this.jTextField3.setText(null);
                        this.jTextField4.setText(null);
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao executar o comando SQL:" + e.toString());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Registro nÃ£o encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.toString());
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
                JOptionPane.showMessageDialog(null, "Nome: " + nome + "\nSalÃ¡rio: R$" + salario + "\nDepartamento: " + departamento);
                this.jTextField1.setText(null);
                this.jTextField2.setText(null);
                this.jTextField3.setText(null);
                this.jTextField4.setText(null);
            } else {
                JOptionPane.showMessageDialog(null, "Registro nÃ£o encontrado...");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar o comando SQL:" + e.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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
        jLabel1.setText("FUNCIONÃ�RIO");

        jLabel2.setText("Nome:");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel3.setText("salÃ¡rio:");

        jLabel4.setText("Departamento:");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton1.setText("Sair");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Limpar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Cadastrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Exibir");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Alterar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Excluir");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel5.setText("CÃ³digo (id):");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton7.setText("Consultar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

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
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JOptionPane.showMessageDialog(null, "Saindo...");
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jTextField1.setText(null);
        this.jTextField2.setText(null);
        this.jTextField3.setText(null);
        this.jTextField4.setText(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.Conectar();
        this.setDados();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.Conectar();
        this.getDados();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.Conectar();
        this.AlteraDados();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.Conectar();
        this.ExcluirDados();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.Conectar();
        this.ConsultaDados();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

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
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Funcionario().setVisible(true);
            }
        });
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