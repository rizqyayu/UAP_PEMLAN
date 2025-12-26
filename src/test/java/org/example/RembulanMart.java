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


    Color BG_UTAMA = new Color(245,245,245);
    Color BG_FORM  = Color.WHITE;
    Color BTN_BG   = new Color(200,200,200);
    Color TXT      = Color.BLACK;

    JTextField tkode = new JTextField();
    JTextField tnama = new JTextField();
    JTextField tharga = new JTextField();

    JTextField kKode = new JTextField();
    JTextField kJumlah = new JTextField();
    JTextField kNama = new JTextField();
    JTextField kHarga = new JTextField();
    DefaultTableModel strukModel;
    JLabel totalLabel = new JLabel("TOTAL : 0");

    DefaultTableModel masterModel;
    JTable masterTable;
    JTextField tcari = new JTextField(10);

    public RembulanMart(){
        setTitle("Rembulan Mart");
        setSize(900,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(BG_UTAMA);

        loadMaster();

        main.add(login(),"login");
        main.add(dashboard(),"dash");
        main.add(tambahData(),"tambah");
        main.add(lihatData(),"lihat");
        main.add(kasir(),"kasir");

        add(main);
        card.show(main,"login");

        kNama.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){
                for(String[] d: master.values()){
                    if(d[1].toLowerCase().startsWith(kNama.getText().toLowerCase())){
                        kKode.setText(d[0]);
                        kHarga.setText(d[2]);
                        break;
                    }
                }
            }
        });
    }

    JPanel login(){
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(BG_UTAMA);

        JPanel p = new JPanel(null);
        p.setPreferredSize(new Dimension(400,300));
        p.setBackground(BG_FORM);

        JLabel t = new JLabel("LOGIN",SwingConstants.CENTER);
        t.setBounds(150,20,100,30);
        t.setForeground(TXT);

        JTextField u = new JTextField();
        JPasswordField pw = new JPasswordField();

        JLabel lu = new JLabel("Username");
        JLabel lp = new JLabel("Password");

        lu.setBounds(60,70,100,20);
        u.setBounds(60,90,280,28);
        lp.setBounds(60,130,100,20);
        pw.setBounds(60,150,280,28);

        JButton b = new JButton("LOGIN");
        b.setBounds(150,210,100,30);
        b.setBackground(BTN_BG);
        b.setForeground(TXT);
        b.setFocusPainted(false);

        b.addActionListener(e->{
            if(u.getText().equals("admin") &&
                    new String(pw.getPassword()).equals("12345"))
                card.show(main,"dash");
            else
                JOptionPane.showMessageDialog(this,
                        "Username atau Password salah",
                        "Login Gagal",
                        JOptionPane.WARNING_MESSAGE);
        });

        p.add(t); p.add(lu); p.add(u);
        p.add(lp); p.add(pw); p.add(b);
        wrap.add(p);
        return wrap;
    }

    JPanel dashboard(){
        JPanel p=new JPanel(null);
        p.setBackground(BG_UTAMA);

        JLabel judul = new JLabel("KASIR REMBULANMART", SwingConstants.CENTER);
        judul.setBounds(0, 20, 900, 40); // tengah atas
        judul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        judul.setForeground(TXT);
        p.add(judul);

        JButton a=new JButton("TAMBAH DATA");
        JButton b=new JButton("KASIR");
        JButton c=new JButton("LIHAT DATA");
        JButton d=new JButton("LOGOUT");

        JButton[] btn={a,b,c,d};
        for(JButton x:btn){
            x.setBackground(BTN_BG);
            x.setForeground(TXT);
            x.setFocusPainted(false);
        }

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

    JPanel tambahData(){
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(BG_UTAMA);

        // container utama
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(500, 250));
        container.setBackground(BG_UTAMA);

        // ===== JUDUL DI LUAR PANEL FORM =====
        JLabel judul = new JLabel("TAMBAH DATA", SwingConstants.CENTER);
        judul.setFont(new Font("Segoe UI", Font.BOLD, 24));
        judul.setForeground(TXT);
        judul.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        container.add(judul, BorderLayout.NORTH);

        // ===== PANEL FORM =====
        JPanel p = new JPanel(null);
        p.setBackground(BG_FORM);

        JLabel l1 = new JLabel("Kode Barang");
        JLabel l2 = new JLabel("Nama Barang");
        JLabel l3 = new JLabel("Harga");

        l1.setBounds(40, 20, 120, 25);
        tkode.setBounds(160, 20, 200, 25);

        l2.setBounds(40, 60, 120, 25);
        tnama.setBounds(160, 60, 200, 25);

        l3.setBounds(40, 100, 120, 25);
        tharga.setBounds(160, 100, 200, 25);

        JButton s = new JButton("SIMPAN");
        JButton k = new JButton("KEMBALI");

        s.setBounds(90, 150, 110, 30);
        k.setBounds(220, 150, 110, 30);

        s.setBackground(BTN_BG);
        k.setBackground(BTN_BG);
        s.setFocusPainted(false);
        k.setFocusPainted(false);

        s.addActionListener(e -> simpanMaster());
        k.addActionListener(e -> card.show(main, "dash"));

        p.add(l1); p.add(tkode);
        p.add(l2); p.add(tnama);
        p.add(l3); p.add(tharga);
        p.add(s); p.add(k);

        container.add(p, BorderLayout.CENTER);
        wrap.add(container);

        return wrap;
    }

    JPanel lihatData(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(BG_UTAMA);

        masterModel=new DefaultTableModel(
                new String[]{"Kode","Nama","Harga"},0);
        masterTable=new JTable(masterModel);
        masterTable.setBackground(BG_FORM);

        JPanel top=new JPanel();
        top.setBackground(BG_FORM);

        JButton cari=new JButton("CARI");
        JButton uk=new JButton("URUT KODE");
        JButton un=new JButton("URUT NAMA");

        JButton[] btn={cari,uk,un};
        for(JButton x:btn){
            x.setBackground(BTN_BG);
            x.setFocusPainted(false);
        }

        top.add(tcari);top.add(cari);
        top.add(uk);top.add(un);

        cari.addActionListener(e->cariMaster());
        uk.addActionListener(e->urut(0));
        un.addActionListener(e->urut(1));

        JPanel bot=new JPanel();
        bot.setBackground(BG_FORM);

        JButton edit=new JButton("EDIT");
        JButton hapus=new JButton("HAPUS");
        JButton back=new JButton("KEMBALI");

        JButton[] btn2={edit,hapus,back};
        for(JButton x:btn2){
            x.setBackground(BTN_BG);
            x.setFocusPainted(false);
        }

        edit.addActionListener(e->editMaster());
        hapus.addActionListener(e->hapusMaster());
        back.addActionListener(e->card.show(main,"dash"));

        bot.add(edit);bot.add(hapus);bot.add(back);

        p.add(top,BorderLayout.NORTH);
        p.add(new JScrollPane(masterTable),BorderLayout.CENTER);
        p.add(bot,BorderLayout.SOUTH);
        return p;
    }

    JPanel kasir(){
        JPanel p=new JPanel(null);
        p.setBackground(BG_UTAMA);

        addField(p,"Kode Barang",kKode,20);
        addField(p,"Nama",kNama,60);
        addField(p,"Jumlah",kJumlah,100);
        addField(p,"Harga",kHarga,140);

        kNama.setEditable(true);
        kHarga.setEditable(false);

        JButton add=new JButton("TAMBAH KE STRUK");
        add.setBounds(600,40,200,30);
        add.setBackground(BTN_BG);
        add.setFocusPainted(false);

        strukModel=new DefaultTableModel(
                new String[]{"Kode","Nama","Jumlah","Harga","Total"},0);
        JTable table=new JTable(strukModel);
        table.setBackground(BG_FORM);

        JScrollPane sp=new JScrollPane(table);
        sp.setBounds(20,200,850,220);

        totalLabel.setBounds(600,170,200,25);

        JButton cetak=new JButton("CETAK STRUK");
        cetak.setBounds(600,80,200,30);
        cetak.setBackground(BTN_BG);
        cetak.setFocusPainted(false);

        JButton kembali = new JButton("KEMBALI");
        kembali.setBounds(600,120,200,30);
        kembali.setBackground(BTN_BG);
        kembali.setForeground(TXT);
        kembali.setFocusPainted(false);

        kembali.addActionListener(e -> card.show(main,"dash"));

        add.addActionListener(e->tambahStruk());
        cetak.addActionListener(e->cetakStruk());

        p.add(add); p.add(cetak); p.add(kembali); p.add(totalLabel); p.add(sp);

        return p;
    }

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
        if(kJumlah.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Jumlah harus diisi",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(!master.containsKey(kKode.getText())){
            JOptionPane.showMessageDialog(this,"Barang tidak ditemukan");
            return;
        }

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

    public static void main(String[] args){ new RembulanMart().setVisible(true);
    }
}
