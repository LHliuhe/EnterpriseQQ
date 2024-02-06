package com.mingrisoft.system;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.mingrisoft.EQ;
import com.mingrisoft.frame.TelFrame;
import com.mingrisoft.userList.ChatLog;
import com.mingrisoft.userList.ChatTree;
import com.mingrisoft.userList.User;

/**
 * 
 * @author Administrator
 *
 */
public class Resource {
    /**
     * �����û�
     * 
     * @param tree
     *            -�û���
     * @param progressBar
     *            -������
     * @param list
     *            - ������û��б�
     * @param button
     *            -��������İ�ť
     * @param ipStart
     *            -��ʼIP��ַ
     * @param ipEnd
     *            -����IP��ַ
     */
    public static void searchUsers(ChatTree tree, JProgressBar progressBar, JList list, JToggleButton button,
            String ipStart, String ipEnd) {
        String[] is = ipStart.split("\\.");// �����ַ�"."��IP��ַ���зָ�
        String[] ie = ipEnd.split("\\.");
        int[] ipsInt = new int[4];// ��ʼ��ַ�� �����ĸ�IP���ֵ�����
        int[] ipeInt = new int[4];// ������ַ�� �����ĸ�IP���ֵ�����
        for (int i = 0; i < 4; i++) {// Ϊ���鸳ֵ
            ipsInt[i] = Integer.parseInt(is[i]);
            ipeInt[i] = Integer.parseInt(ie[i]);
        }
        progressBar.setIndeterminate(true);// ʹ�ò�ȷ��������
        progressBar.setStringPainted(true);// �������п���չʾ����
        DefaultListModel model = new DefaultListModel();// ����������ģ��
        model.addElement("���������");// ��һ�����˵������
        list.setModel(model);// �����û����������ģ��
        try {
            // �����ĸ�����ֵ���ֱ𴢴�IP��ַ���ĸ���ֵ��Ҳ��ѭ���ĳ�ʼֵ
            int a = ipsInt[0], b = ipsInt[1], c = ipsInt[2], d = ipsInt[3];
            // ѭ��IP��ַ�ĵ�һλ���֣�ѭ����ΧС���û��趨�����ֵ��ѭ����ǩΪone
            one: while (a <= ipeInt[0]) {
                // ѭ��IP��ַ�ĵڶ�λ���֣�ѭ����ΧС�ڵ���255��ѭ����ǩΪtwo
                two: while (b <= 255) {
                    // ѭ��IP��ַ�ĵ���λ���֣�ѭ����ΧС�ڵ���255��ѭ����ǩΪthree
                    three: while (c <= 255) {
                        // ѭ��IP��ַ�ĵ���λ���֣�ѭ����ΧС�ڵ���255��ѭ����ǩΪfour
                        four: while (d <= 255) {
                            if (!button.isSelected()) {// ��������û���ť�Ƿ�ѡ��״̬
                                progressBar.setIndeterminate(false);// �������ָ�ȷ��ʽ�Ľ�����
                                return;// ��������
                            }
                            Thread.sleep(100);// ����100����
                            String ip = a + "." + b + "." + c + "." + d;// ƴ��ѭ����õ�IP��ַ
                            progressBar.setString("����������" + ip);// ��������ʾ�ı�
                            if (tree.addUser(ip, "search"))// ���������ӵ���IP�ϵ��û�
                                model.addElement("<html><b><font color=green>���" + ip + "</font></b></html>");// ����ģ�����������Ϣ
                            // ���ѭ����IP��ַ�ѵ����û�ָ�������Χ
                            if (a == ipeInt[0] && b == ipeInt[1] && c == ipeInt[2] && d == ipeInt[3]) {
                                break one;// ������ǩΪone��ѭ����Ҳ���ǽ��������ѭ��
                            }
                            d++;// ����λ��������
                            if (d > 255) {// �������λ���ֳ���255
                                d = 0;// ����λ���ֹ���
                                break four;// �������Ĳ�ѭ��
                            }
                        }
                        c++;// ����λ���ֵ���
                        if (c > 255) {// �������λ���ֳ���255
                            c = 0;// ����λ���ֹ���
                            break three;// ����������ѭ��
                        }
                    }
                    b++;// �ڶ�λ���ֵ���
                    if (b > 255) {// ����ڶ�λ���ֳ���255
                        b = 0;// �ڶ�λ���ֹ���
                        break two;// �����ڶ���ѭ��
                    }
                }
                a++;// ��һλ���ֵ���
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            progressBar.setIndeterminate(false);
            progressBar.setString("�������");
            button.setText("�������û�");
            button.setSelected(false);
        }
    }

    /**
     * ��ϢȺ�����˹��ܿ���Ⱥ��֪ͨ��ÿһ���յ���Ϣ���û����ᵯ������Ự��չʾȺ������Ϣ
     * 
     * @param ss
     *            - UDP�׽���
     * @param selectionPaths
     *            - ��ѡ�е��û��ڵ�·��
     * @param message
     *            - Ⱥ������Ϣ
     */
    public static void sendGroupMessenger(final DatagramSocket ss, final TreePath[] selectionPaths,
            final String message) {

        new Thread(new Runnable() {
            public void run() {
                MessageFrame messageFrame = new MessageFrame();// ����Ⱥ�Ĵ���
                messageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                try {
                    for (TreePath path : selectionPaths) {// ������ѡ�еĽڵ�·��
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();// ���ش�·�������һ���������ǿ��ת���ɽڵ�
                        User user = (User) node.getUserObject();// ���ڵ��б���Ķ���ת�����û�����
                        messageFrame.setStateBarInfo(
                                "<html>���ڸ�<font color=blue>" + user.getName() + "</font>������Ϣ����</html>");// ����״̬����Ϣ
                        Thread.sleep(20);// ����20����
                        byte[] strData = message.getBytes();// ��ҪȺ������ϢתΪ�ֽ�����
                        InetAddress toAddress = InetAddress.getByName(user.getIp());// ����Ŀ���û�IP��ַ����
                        DatagramPacket tdp = null;// ����UDP���ݰ�
                        // ��ʼ�����ݰ����������������飬���鳤�ȣ�Ҫ���͵ĵ�ַ
                        tdp = new DatagramPacket(strData, strData.length, toAddress, 1111);
                        ss.send(tdp);// �������ݰ�
                        messageFrame.addMessage(user.getName() + " ������ϣ�", true);// ��Ⱥ����Ϣ������ӷ����������
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ����
                        String title = user.getName() + "  (" + sdf.format(new Date()) + ")";// �û����ƺ����������
                        ChatLog.writeLog(user.getIp(), title);// ��¼�������¼�е��û�����ʱ��
                        ChatLog.writeLog(user.getIp(), "[Ⱥ������] " + message);// ��¼�������¼��Ⱥ������
                    }
                    messageFrame.setStateBarInfo("��Ϣ�������,���Թرմ��ڡ�");// �޸�Ⱥ����Ϣ����״̬��
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();// �����߳�
    }

    /**
     * �򿪶Է������ļ���
     * 
     * @param str
     *            �Է�IP
     */
    public static void startFolder(String str) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + str);// ����windows��cmd������ļ���
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean loginPublic(String user, String pass) {// ��½����������
        try {
            String userName = user;
            String updatePath = EQ.preferences.get("updatePath", null);
            if (updatePath == null)
                return false;
            File file = new File(updatePath);
            if (!file.exists())
                return false;
            if (file.isFile())
                updatePath = file.getParent();
            if (userName != null && !userName.equals("")) {
                userName = " /user:" + userName;
            }
            Process process = Runtime.getRuntime().exec("cmd /c %windir%" + File.separator + "System32" + File.separator
                    + "net use " + updatePath + " " + pass + userName);
            System.out.println("cmd /c %windir%" + File.separator + "System32" + File.separator + "net use "
                    + updatePath + " " + pass + userName);
            Scanner sce = new Scanner(process.getErrorStream());
            StringBuilder stre = new StringBuilder();
            while (sce.hasNextLine()) {
                stre.append(sce.nextLine());
            }
            process.destroy();
            String resulte = stre.toString();
            if (resulte.equals(""))
                return true;
            else
                JOptionPane.showMessageDialog(EQ.frame, resulte, "������Ϣ", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendMessenger(User user, String message, TelFrame frame) {// ������ʹ��Ϣ
        class TheThread implements Runnable {
            private User user;
            private String message;
            private TelFrame frame;
            private JButton sendButton;

            public TheThread(User user, String message, TelFrame frame) {
                this.user = user;
                this.message = message;
                this.frame = frame;
                this.sendButton = frame.getSendButton();
            }

            public void run() {
                try {
                    sendButton.setEnabled(false);
                    Process process = Runtime.getRuntime().exec("net send " + user.getIp() + " " + message);
                    InputStream is = process.getInputStream();
                    int i, j;
                    StringBuilder sb = new StringBuilder();
                    while ((i = is.read()) != -1) {
                        sb.append((char) i);
                    }
                    String runIs = new String(sb.toString().getBytes("iso-8859-1")).trim().replace(user.getIp(),
                            user.getName());
                    InputStream eis = process.getErrorStream();
                    StringBuilder esb = new StringBuilder();
                    while ((j = eis.read()) != -1) {
                        esb.append((char) j);
                    }
                    String runEis = new String(esb.toString().getBytes("iso-8859-1")).trim().replace(user.getIp(),
                            user.getName());
                    frame.appendReceiveText(runIs, new Color(187, 30, 193));
                    if (runEis.length() > 0)
                        frame.appendReceiveText(runIs, Color.RED);
                    sendButton.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        new Thread(new TheThread(user, message, frame)).start();
    }
}
