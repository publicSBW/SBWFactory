package com.itheima.store.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.itheima.store.domain.Category;
import com.itheima.store.domain.Product;
import com.itheima.store.service.CategoryService;
import com.itheima.store.service.ProductService;
import com.itheima.store.utils.BeanFactory;
import com.itheima.store.utils.UUIDUtils;

/**
 * 添加商品的Servlet
 * @author 趴布里克
 *
 */
public class AddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			//创建一个Product对象
			Product product = new Product();
			
			//一、创建磁盘文件项工程
			DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
			//设置缓存区大小
			diskFileItemFactory.setSizeThreshold(1024 * 1024 * 3);
			//设置临时文件存放位置
			//diskFileItemFactory.setRepository(repository);
			
			//二、获得核心解析类
			ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
			//解决中文文件上传乱码
			fileUpload.setHeaderEncoding("UTF-8");
			//设置单个文件大小
			//fileUpload.setFileSizeMax(fileSizeMax);
			//设置表单中的所有文件项的文件总大小
			//fileUpload.setSizeMax(sizeMax);
			
			//三、解析request，返回list
			List<FileItem> list = fileUpload.parseRequest(request);
			//遍历集合
			//将遍历的值存入map集合中
			Map<String, String> map = new HashMap<String, String>();
			String fileName = null;
			for (FileItem fileItem : list) {
				//判断普通项和文件上传项
				if(fileItem.isFormField()) {
					//普通项
					String name = fileItem.getFieldName();
					String value = fileItem.getString("UTF-8"); // 方法的重载,解决的是普通项的中文乱码.
					System.out.println(name+"   "+value);
					map.put(name, value);
				}else {
					//文件上传项
					//获得文件名
					fileName = fileItem.getName();
					System.out.println("文件名：" + fileName);
					//获得文件上传的输入流
					InputStream is = fileItem.getInputStream();
					//获得文件上传的路径
					String path = this.getServletContext().getRealPath("/products/1");
					//获得文件上传的输出流
					OutputStream os = new FileOutputStream(path+"/"+fileName);
					
					//流的对接-->读写
					int len;
					byte[] bys = new byte[1024];
					while((len = is.read(bys)) != -1) {
						os.write(bys, 0, len);
					}
					os.close();
					is.close();
				}
			}
			
			//封装product对象
			BeanUtils.populate(product, map);
			product.setPid(UUIDUtils.getUUID());
			product.setPdate(new Date());
			product.setPflag(0);
			product.setPimage("products/1/"+fileName);
			Category category = new Category();
			category.setCid(map.get("cid"));
			product.setCategory(category);
			//存入数据库
			ProductService productService = (ProductService) BeanFactory.getBean("productService");
			productService.save(product);
			
			//页面条状
			response.sendRedirect(request.getContextPath()+"/AdminProductServlet?method=findByPage&currPage=1");
			
		}catch(Exception e) {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
