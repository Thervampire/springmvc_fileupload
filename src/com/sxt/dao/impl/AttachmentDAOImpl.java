package com.sxt.dao.impl;

import java.util.List;

import com.sxt.bean.Attachment;
import com.sxt.dao.AttachmentDAO;
import com.sxt.utils.DBUtils;
import org.junit.Test;

public class AttachmentDAOImpl implements AttachmentDAO {

	@Override
	public void add(Attachment bean) {
		String sql="INSERT INTO ATTACHMENT(FOLDNAME,FNEWNAME,FCONTENTTYPE,FPATH) VALUES(?,?,?,?)";
		Object[] objs={bean.getFoldname(),bean.getFnewname(),bean.getFcontenttype(),bean.getFpath()};
		DBUtils.executeUpdate(sql, objs);
	}

	@Override
	public void update(Attachment bean) {
		String sql="UPDATE ATTACHMENT SET FOLDNAME=?,FNEWNAME=?,FCONTENTTYPE=?,FPATH=? WHERE FID=?";
		Object[] objs={bean.getFoldname(),bean.getFnewname(),bean.getFcontenttype(),bean.getFpath(),bean.getFid()};
		DBUtils.executeUpdate(sql, objs);
	}

	@Override
	public void delete(Attachment bean) {
		String sql="DELETE FROM  ATTACHMENT  WHERE FID=?";
		Object[] objs={bean.getFid()};
		DBUtils.executeUpdate(sql, objs);
	}

	@Override
	public List<Attachment> query() {
		String sql="SELECT * FROM ATTACHMENT";
		return DBUtils.getList(sql, null, Attachment.class);
	}

	@Override
	public Attachment queryById(Attachment bean) {
		String sql="SELECT * FROM ATTACHMENT WHERE FID=?";
		return DBUtils.getBean(sql, new Object[]{bean.getFid()}, Attachment.class);
	}

}
