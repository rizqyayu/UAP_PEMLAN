package org.example;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class RembulanMart extends JFrame {

    CardLayout card = new CardLayout();
    JPanel main = new JPanel(card);

    final String MASTER = "barang_master.txt";

    HashMap<String,String[]> master = new HashMap<>();

    // === TAMBAH DATA ===
    JTextField tkode = new JTextField();
    JTextField tnama = new JTextField();
    JTextField tharga = new JTextField();

    // === KASIR ===
    JTextField kKode = new JTextField();
    JTextField kJumlah = new JTextField();
    JTextField kNama = new JTextField();
    JTextField kHarga = new JTextField();
    DefaultTableModel strukModel;
    JLabel totalLabel = new JLabel("TOTAL : 0");

    // === LIHAT DATA ===
    DefaultTableModel masterModel;
    JTable masterTable;
    JTextField tcari = new JTextField(10);

    public RembulanMart(){
        setTitle("Rembulan Mart");
        setSize(900,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        loadMaster();

        main.add(login(),"login");
        main.add(dashboard(),"dash");
        main.add(tambahData(),"tambah");
        main.add(lihatData(),"lihat");
        main.add(kasir(),"kasir");

        add(main);
        card.show(main,"login");
    }

    // ================= LOGIN =================
    JPanel login(){
        JPanel p=new JPanel(null);

        JLabel t=new JLabel("LOGIN",SwingConstants.CENTER);
        t.setBounds(350,40,200,30);

        JTextField u=new JTextField();
        JPasswordField pw=new JPasswordField();

        addField(p,"Username",u,120);
        addField(p,"Password",pw,170);

        JButton b=new JButton("LOGIN");
        b.setBounds(380,230,120,30);

        b.addActionListener(e->{
            if(u.getText().equals("admin") &&
                    new String(pw.getPassword()).equals("12345"))
                card.show(main,"dash");
            else
                JOptionPane.showMessageDialog(this,"Login gagal",
                        "Error",JOptionPane.WARNING_MESSAGE);
        });

        p.add(t);p.add(b);
        return p;
    }

    // ================= DASHBOARD =================
    JPanel dashboard(){
        JPanel p=new JPanel(null);

        JButton a=new JButton("TAMBAH DATA");
        JButton b=new JButton("KASIR");
        JButton c=new JButton("LIHAT DATA");
        JButton d=new JButton("LOGOUT");

        a.setBounds(350,80,200,35);
        b.setBounds(350,130,200,35);
        c.setBounds(350,180,200,35);
        d.setBounds(350,230,200,35);

        a.addActionListener(e->card.show(main,"tambah"));
        b.addActionListener(e->card.show(main,"kasir"));
        c.addActionListener(e->{refreshMaster();card.show(main,"lihat");});
        d.addActionListener(e->card.show(main,"login"));

        p.add(a);p.add(b);p.add(c);p.add(d);
        return p;
    }

    // ================= TAMBAH DATA =================
    JPanel tambahData(){
        JPanel p=new JPanel(null);

        addField(p,"Kode Barang",tkode,80);
        addField(p,"Nama Barang",tnama,120);
        addField(p,"Harga",tharga,160);

        JButton s=new JButton("SIMPAN");
        JButton k=new JButton("KEMBALI");

        s.setBounds(300,220,130,30);
        k.setBounds(450,220,130,30);

        s.addActionListener(e->simpanMaster());
        k.addActionListener(e->card.show(main,"dash"));

        p.add(s);p.add(k);
        return p;
    }

    // ================= LIHAT DATA =================
    JPanel lihatData(){
        JPanel p=new JPanel(new BorderLayout());

        masterModel=new DefaultTableModel(
                new String[]{"Kode","Nama","Harga"},0);
        masterTable=new JTable(masterModel);

        JPanel top=new JPanel();
        JButton cari=new JButton("CARI");
        JButton uk=new JButton("URUT KODE");
        JButton un=new JButton("URUT NAMA");

        top.add(tcari);top.add(cari);top.add(uk);top.add(un);

        cari.addActionListener(e->cariMaster());
        uk.addActionListener(e->urut(0));
        un.addActionListener(e->urut(1));

        JPanel bot=new JPanel();
        JButton edit=new JButton("EDIT");
        JButton hapus=new JButton("HAPUS");
        JButton back=new JButton("KEMBALI");

        edit.addActionListener(e->editMaster());
        hapus.addActionListener(e->hapusMaster());
        back.addActionListener(e->card.show(main,"dash"));

        bot.add(edit);bot.add(hapus);bot.add(back);

        p.add(top,BorderLayout.NORTH);
        p.add(new JScrollPane(masterTable),BorderLayout.CENTER);
        p.add(bot,BorderLayout.SOUTH);
        return p;
    }

    // ================= KASIR =================
    JPanel kasir(){
        JPanel p=new JPanel(null);

        addField(p,"Kode Barang",kKode,20);
        addField(p,"Jumlah",kJumlah,60);
        addField(p,"Nama",kNama,100);
        addField(p,"Harga",kHarga,140);

        kNama.setEditable(false);
        kHarga.setEditable(false);

        kKode.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                if(master.containsKey(kKode.getText())){
                    String[] d=master.get(kKode.getText());
                    kNama.setText(d[1]);
                    kHarga.setText(d[2]);
                }
            }
        });

        JButton add=new JButton("TAMBAH KE STRUK");
        add.setBounds(600,40,200,30);

        strukModel=new DefaultTableModel(
                new String[]{"Kode","Nama","Jumlah","Harga","Total"},0);
        JTable table=new JTable(strukModel);

        JScrollPane sp=new JScrollPane(table);
        sp.setBounds(20,200,850,220);

        totalLabel.setBounds(600,170,200,25);

        JButton cetak=new JButton("CETAK STRUK");
        cetak.setBounds(600,80,200,30);

        add.addActionListener(e->tambahStruk());
        cetak.addActionListener(e->cetakStruk());

        p.add(add);p.add(cetak);
        p.add(totalLabel);p.add(sp);
        return p;
    }

    // ================= LOGIC =================
    void simpanMaster(){
        String k=tkode.getText();
        if(master.containsKey(k)){
            JOptionPane.showMessageDialog(this,"Kode sudah ada");
            return;
        }
        String[] d={k,tnama.getText(),tharga.getText()};
        master.put(k,d);
        try(FileWriter fw=new FileWriter(MASTER,true)){
            fw.write(String.join(",",d)+"\n");
        }catch(Exception e){}
        refreshMaster();
        card.show(main,"lihat");
        tkode.setText("");tnama.setText("");tharga.setText("");
    }

    void refreshMaster(){
        masterModel.setRowCount(0);
        for(String[] d:master.values())
            masterModel.addRow(d);
    }

    void cariMaster(){
        masterModel.setRowCount(0);
        for(String[] d:master.values())
            if(d[0].contains(tcari.getText())||
                    d[1].toLowerCase().contains(tcari.getText().toLowerCase()))
                masterModel.addRow(d);
    }

    void urut(int i){
        ArrayList<String[]> list=new ArrayList<>(master.values());
        list.sort(Comparator.comparing(a->a[i]));
        masterModel.setRowCount(0);
        for(String[] d:list) masterModel.addRow(d);
    }

    void editMaster(){
        int r=masterTable.getSelectedRow();
        if(r==-1)return;
        String kode=masterModel.getValueAt(r,0).toString();
        String nama=JOptionPane.showInputDialog("Nama baru",
                masterModel.getValueAt(r,1));
        String harga=JOptionPane.showInputDialog("Harga baru",
                masterModel.getValueAt(r,2));
        master.put(kode,new String[]{kode,nama,harga});
        simpanUlangMaster();
        refreshMaster();
    }

    void hapusMaster(){
        int r=masterTable.getSelectedRow();
        if(r==-1)return;
        master.remove(masterModel.getValueAt(r,0));
        simpanUlangMaster();
        refreshMaster();
    }

    void simpanUlangMaster(){
        try(FileWriter fw=new FileWriter(MASTER)){
            for(String[] d:master.values())
                fw.write(String.join(",",d)+"\n");
        }catch(Exception e){}
    }

    void tambahStruk(){
        int j=Integer.parseInt(kJumlah.getText());
        int h=Integer.parseInt(kHarga.getText());
        int t=j*h;
        strukModel.addRow(new Object[]{
                kKode.getText(),kNama.getText(),j,h,t
        });
        hitungTotal();
        kKode.setText("");kJumlah.setText("");
        kNama.setText("");kHarga.setText("");
    }

    void hitungTotal(){
        int sum=0;
        for(int i=0;i<strukModel.getRowCount();i++)
            sum+=Integer.parseInt(
                    strukModel.getValueAt(i,4).toString());
        totalLabel.setText("TOTAL : "+sum);
    }

    void cetakStruk(){
        try(FileWriter fw=new FileWriter(TRANS,true)){
            for(int i=0;i<strukModel.getRowCount();i++){
                fw.write(
                        strukModel.getValueAt(i,0)+","+
                                strukModel.getValueAt(i,1)+","+
                                strukModel.getValueAt(i,2)+","+
                                strukModel.getValueAt(i,3)+","+
                                strukModel.getValueAt(i,4)+"\n"
                );
            }
        }catch(Exception e){}
        JOptionPane.showMessageDialog(this,"Struk dicetak");
        strukModel.setRowCount(0);
        totalLabel.setText("TOTAL : 0");
    }

    void loadMaster(){
        try(BufferedReader br=new BufferedReader(new FileReader(MASTER))){
            String l;
            while((l=br.readLine())!=null){
                String[] d=l.split(",");
                master.put(d[0],d);
            }
        }catch(Exception e){}
    }

    void addField(JPanel p,String l,JComponent f,int y){
        JLabel lb=new JLabel(l);
        lb.setBounds(80,y,120,25);
        f.setBounds(200,y,300,28);
        p.add(lb);p.add(f);
    }

    public static void main(String[] args){
        new RembulanMart().setVisible(true);
    }
}
