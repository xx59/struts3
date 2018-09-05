package  stu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class StuInfo extends JFrame implements ActionListener{

    private int flag;

    private String stuId,stuName,stuSex,stuOri;
    private int stuAge;

    private JButton displayBtn,addBtn,searchBtn,deleteBtn,updateBtn;
    private JTextArea displayJta;
    private JScrollPane jsp;

    public  StuInfo(){

        super("学生管理工具");

        displayBtn = new JButton("浏览");
        displayBtn.addActionListener(this);

        addBtn = new JButton("增加");
        addBtn.addActionListener(this);

        searchBtn = new JButton("查看");
        searchBtn.addActionListener(this);

        deleteBtn = new JButton("删除");
        deleteBtn.addActionListener(this);

        updateBtn = new JButton("更改");
        updateBtn.addActionListener(this);

        displayJta = new JTextArea(10,40);
        jsp = new JScrollPane(displayJta);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());
        container.add(displayBtn);
        container.add(addBtn);
        container.add(searchBtn);
        container.add(deleteBtn);
        container.add(updateBtn);
        container.add(jsp);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(480,280);
        this.setLocation((screenSize.width-480)/2,(screenSize.height-280)/2);
        setVisible(true);
    }


    class AddOrUpdate extends JDialog implements ActionListener{

        JLabel idLable,nameLable,sexLable,ageLable,oriLable;
        JTextField idJtf,nameJtf,sexJtf,ageJtf,oriJtf;
        JButton okBtn,cancelBtn;
        JFrame jf;

        public AddOrUpdate(JFrame jf,String title,boolean model) {
            super(jf,title,model);
            this.jf = jf;
            idLable = new JLabel("学号:") ;
            nameLable = new JLabel("姓名:") ;
            sexLable = new JLabel("性别:") ;
            ageLable = new JLabel("年龄:") ;
            oriLable = new JLabel("籍贯:") ;

            if(flag == 2){
                idJtf = new JTextField(10);
                nameJtf = new JTextField(10);
                sexJtf = new JTextField(10);
                ageJtf = new JTextField(10);
                oriJtf = new JTextField(10);
            }else{
                idJtf = new JTextField(stuId,10);
                nameJtf = new JTextField(stuName,10);
                sexJtf = new JTextField(stuSex,10);
                ageJtf = new JTextField(stuAge+"",10);
                oriJtf = new JTextField(stuOri,10);

                idJtf.setEditable(false);

            }

            okBtn = new JButton("确定");
            okBtn.addActionListener(this);
            cancelBtn = new JButton("取消");
            cancelBtn.addActionListener(this);

            Container container = this.getContentPane();
            container.setLayout(new FlowLayout());
            container.add(idLable);
            container.add(idJtf);
            container.add(nameLable);
            container.add(nameJtf);
            container.add(sexLable);
            container.add(sexJtf);
            container.add(ageLable);
            container.add(ageJtf);
            container.add(oriLable);
            container.add(oriJtf);
            container.add(okBtn);
            container.add(cancelBtn);


            this.setSize(180,210);
            this.setLocation(jf.getLocation().x+(jf.getWidth()-180)/2,jf.getLocation().y+(jf.getHeight()-210)/2);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            String sql,tid,tname,tsex,tage,tori;
            tid = idJtf.getText();
            tname = nameJtf.getText();
            tsex = sexJtf.getText();
            tage = ageJtf.getText();
            tori = oriJtf.getText();

            if(flag == 2){
                sql = "INSERT INTO stuinfo VALUES('" + tid + "','" + tname + "','" + tsex + "','" + tage + "','" + tori + "')";
            }else{
                sql = "UPDATE stuinfo SET StuName = '" + tname +"',StuSex = '" + tsex +"',StuAge = '" + tage +"',StuOri = '" + tori +"'WHERE StuID ='"+ tid + "'";
            }

            if(arg0.getSource() == okBtn){
                setVisible(false);
                UpdateStuInfo(sql);
            }
            else{
                setVisible(false);
            }
        }

    }

    class SearchOrDeleteOrUpdate extends JDialog implements ActionListener{

        JLabel idLabel;
        JTextField idJtf;
        JButton okBtn,cancelBtn;
        JFrame jf;

        public SearchOrDeleteOrUpdate(JFrame jf,String title,boolean model){
            super(jf,title,model);
            this.jf = jf;
            idLabel = new JLabel("学号");
            idJtf = new JTextField(10);
            okBtn = new JButton("确定");
            okBtn.addActionListener(this);
            cancelBtn = new JButton("取消");
            cancelBtn.addActionListener(this);

            Container container = this.getContentPane();
            container.setLayout(new FlowLayout());
            container.add(idLabel);
            container.add(idJtf);
            container.add(okBtn);
            container.add(cancelBtn);

            this.setSize(180, 130);
            this.setLocation(jf.getLocation().x+(jf.getWidth()-180)/2,jf.getLocation().y+(jf.getHeight()-130)/2);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub
            String sid = idJtf.getText();
            if(arg0.getSource() == okBtn){
                if(sid.length()!=0){
                    setVisible(false);
                    SearchStuInfo(sid);
                }else{
                    JOptionPane.showMessageDialog(null,"学号不能为空","提示ʾ" ,JOptionPane.ERROR_MESSAGE);

                }
            }
            else{
                setVisible(false);
            }
        }

    }

    public void DisplayStuInfo(){
        ConnDB connDb = new ConnDB();
        try{
            String sql,stuId,stuName,stuSex,stuOri;
            int stuAge;
            String str = "学号"+"\t"+"姓名"+"\t"+"性别"+"\t"+"年龄"+"\t"+"籍贯"+"\n";
            sql = "SELECT * FROM stuinfo";

            ResultSet rs = connDb.Query(sql);
            while (rs.next()) {
                stuId = rs.getString("StuID");
                stuName = rs.getString("StuName");
                stuSex = rs.getString("StuSex");
                stuAge = rs.getInt("StuAge");
                stuOri = rs.getString("StuOri");
                str+=stuId + "\t" + stuName + "\t" + stuSex + "\t" + stuAge + "\t" + stuOri +"\n" ;
            }
            displayJta.setText(str);
            rs.close();
            connDb.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void UpdateStuInfo(String sql){

        try{
            ConnDB connDb = new ConnDB();
            connDb.Update(sql);
            if(flag ==2){
                displayJta.setText("成功增加学生信息！");
            }else if(flag == 4){
                displayJta.setText("成功删除学生信息！");
            }else{
                displayJta.setText("成功修改学生信息！");
            }
            connDb.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this,"该学生已经存在！","提示" ,1);
        }

    }

    public void SearchStuInfo(String sid){
        ConnDB connDb = new ConnDB();
        String sql = "SELECT * FROM stuinfo WHERE StuID = '" + sid + "'";
        try{
            String id="",name="",sex="",str="",ori="";
            int age = 0;
            ResultSet rs = connDb.Query(sql);
            while(rs.next()){
                id = rs.getString("StuID");
                name = rs.getString("StuName");
                sex = rs.getString("StuSex");
                age = rs.getInt("StuAge");
                ori = rs.getString("StuOri");
                str+= id + "\t" + name + "\t" +sex + "\t" + age + "\t" + ori;
            }

            if(str.length()!=0){
                displayJta.setText(str);
                if(flag == 4){
                    int i = JOptionPane.showConfirmDialog(null, "确定删除该学生吗","提示" ,2,1);
                    if(i==0){
                        String dsql = "DELETE FROM stuinfo WHERE StuID = '" + sid +"'";
                        UpdateStuInfo(dsql);
                    }
                }else if(flag ==5){
                    this.stuId = id;
                    this.stuName = name;
                    this.stuSex = sex;
                    this.stuAge = age;
                    this.stuOri = ori;

                    new AddOrUpdate(this, "修改学生信息", true);
                }
            }else{
                displayJta.setText("没有该学生信息！");
            }

            rs.close();
            connDb.close();

        }catch(Exception e){
            System.out.println(e.toString());

        }
    }


    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == displayBtn){
            flag = 1;
            this.DisplayStuInfo();
        }
        if(e.getSource() == addBtn){

            flag =2;
            new AddOrUpdate(this, "增加学生信息", true);
        }
        if(e.getSource() == searchBtn){
            flag =3;
            new SearchOrDeleteOrUpdate(this, "查询学生信息", true);
        }
        if(e.getSource() == deleteBtn){
            flag =4;
            new SearchOrDeleteOrUpdate(this, "删除学生信息", true);
        }
        if(e.getSource() == updateBtn){
            flag =5;
            new SearchOrDeleteOrUpdate(this, "修改学生信息", true);
        }


    }

    static String sql = null;
    static ConnDB db = null;
    static ResultSet resultSet = null;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StuInfo stuInfo = new StuInfo();
    }
}

