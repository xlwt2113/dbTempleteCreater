package com.ssj.jdbcfront.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ssj.jdbcfront.dao.DbUtil;

public class SqlFileReader {
	private BufferedReader in;
	StringBuffer sql = null;
	private List<String> stack = new ArrayList<String>();
	private int sqlType=0;

	public SqlFileReader(String path) throws FileNotFoundException {
		this(new FileReader(path));
	}

	public SqlFileReader(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	public SqlFileReader(Reader in) {
		if (in == null) {
			throw new NullPointerException("inputstream����Ϊ��!");
		}
		if(!(in instanceof BufferedReader)){
			in = new BufferedReader(in);
		}
		this.in = (BufferedReader) in;
	}
	private List<String> multiSqlType = Arrays.asList(new String[]{"PROCEDURE","FUNCTION","PACKAGE","TYPE"});
	/**
	 * 1Ϊ����SQL��� ��2Ϊ�������
	 * @return
	 */
	private int getSqlType(){
		if(sqlType==0){
			String type = null;
			if(stack.size()>=4
					&&"CREATE".equalsIgnoreCase(stack.get(0))
					&&"OR".equals(stack.get(1))
					&&"REPALCE".equals(stack.get(2))){
				type = stack.get(3);
			}
			else if(stack.size()>=2
						&&"CREATE".equalsIgnoreCase(stack.get(0))){
				type = stack.get(1);
			}
			sqlType = type!=null&&(multiSqlType.contains(type))?2:1;
		}
		return sqlType;
	}

    private int read() throws IOException {
		int c = in.read();
		if(marked){
			readAfterMarked = true;
		}
		if(c!=-1){
			sql.append((char)c);
		}
		return c;
    }
    private boolean marked,readAfterMarked;
    private void reset() throws IOException{
    	in.reset();
    	if(readAfterMarked&&sql.length()>0){
    		sql.deleteCharAt(sql.length()-1);
    		readAfterMarked = false;
    	}
    }
    private void mark() throws IOException{
    	in.mark(1);
    	marked = true;
    }
    private String readLine() throws IOException{
    	String line = in.readLine();
    	if(line!=null){
    		sql.append(line);
    	}
    	return line;
    }
    
	public String readSql() throws IOException {
		StringBuffer word = new StringBuffer(10);
		sql = new StringBuffer();
		stack.clear();
		boolean lastSem = false;
		//����������������--ע�ͣ�/*  */ע�ͣ�''�ַ�����SQL����Ƿ�Ϊ�������
			for (;;) {
				int i = read();
				if (i==-1) { /* EOF */
					if (sql != null && sql.toString().trim().length() > 0)
						return sql.toString().trim();
					else
						return null;
				}
				char c = (char)i;
				if(stack.size()<6){//����ǰ6������
					if(c>=0&&c<'!'){//���Ϊ�հ��ַ����򽫵���д�뵥���б�
						if(word.length()>0){
							String w = word.toString().toUpperCase();
							stack.add(w);
							word.setLength(0);
							if(stack.size()==1&&!DbUtil.SQL_PREFIX.contains(w)){
								readLine();
								return sql.toString().trim();
							}
						}
					}
					else{//�����Ϊ�գ����ַ�д�뵥�ʻ���
						if(c<0||c>='!'){
							word.append(c);
						}
					}
				}
				if(c=='\''){//������֣��ͽ����ַ����Ľ���
					int j = 0;
					while((j=read())>0){
						while(j!='\''){
							j=read();
						}
						mark();
						if(read()!='\''){//���'��β�ˣ����ʾ���ַ����Ѿ�����
							reset();
							break;
						}
					}
				}
				if(c=='-'){
					mark();
					if(read()=='-'){//--ע��
						readLine();
						String s = sql.toString().trim();
						if(s.startsWith("--")&&s.indexOf("\\n")<0){
							return s;
						}
					}
					else{
						reset();
					}
				}
				if(c=='/'){
					mark();
					if(read()=='*'){//--ע��
						int j = 0;
						while((j=read())>0){
							while(j!='*'){
								j=read();
							}
							if(read()=='/'){//���*/��β�ˣ����ʾ��ע���Ѿ������������������ע�ͽ�����
								break;
							}
						}
					}
					else{
						reset();
					}
				}
				if(c<0||c>='!'){//�ǿհ��ַ�ʱ���ֺű�־��Ϊfalse
					lastSem = false;
				}
				if(c==';'){
					if(getSqlType()==1){//����ǵ���SQL����ֱ�ӷ���
						return sql.substring(0,sql.length()-1).trim();
					}
					else{
						lastSem = true;
					}
				}
				if(lastSem && c=='/' && getSqlType()==2){//����Ǹ���SQL����ֱ�ӷ���
					return sql.substring(0,sql.length()-1).trim();
				}
			}
	}

	/**
	 * Close the stream.
	 * 
	 * @exception IOException
	 *                If an I/O error occurs
	 */
	public void close() throws IOException {
		if (in == null)
			return;
		in.close();
		in = null;
	}

	public static void main(String[] args) throws Exception {
		SqlFileReader in = new SqlFileReader("C:/2.sql");
		String sql;
		while((sql=in.readSql())!=null){
			System.out.println("================================================================================================================");
			System.out.println(sql);
		}
	}
}
