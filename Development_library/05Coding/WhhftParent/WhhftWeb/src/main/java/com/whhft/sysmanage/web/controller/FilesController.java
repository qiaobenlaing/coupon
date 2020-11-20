package com.whhft.sysmanage.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whhft.sysmanage.common.rmi.NewsCatalogService;
import com.whhft.sysmanage.web.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

/**
 * CKeditor文件上传
 * 
 * @author hft
 * 
 */
@Controller
@RequestMapping("/files")
public class FilesController {

	@Resource
	private NewsCatalogService catalogService;
	// 图片类型
	private static List<String> imageTypes = new ArrayList<String>();
	private static List<String> flashTypes = new ArrayList<String>();
	static {
		imageTypes.add(".jpg");
		imageTypes.add(".jpeg");
		imageTypes.add(".bmp");
		imageTypes.add(".gif");
		imageTypes.add(".png");
		flashTypes.add(".gif");
		flashTypes.add(".swf");
		flashTypes.add(".avi");
		flashTypes.add(".flv");
	}

	@Value(value = "${ckeditor.storage.image.path}")
	private String ckeditorStorageImagePath;

	@Value(value = "${ckeditor.access.image.url}")
	private String ckeditorAccessImageUrl;

	@RequestMapping(value = "/upload/image")
	@ResponseBody
	
	public String uploadImage(@RequestParam("upload") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			String type) throws Exception {		
		response.setContentType("text/html; charset=UTF-8");//必须，否则ckeditor提示有可能乱码
	
		// 解决跨域问题
		//response.setHeader("X-Frame-Options", "SAMEORIGIN");
		/*MultipartRequest mReq=(MultipartRequest) request;
		MultipartFile file =mReq.getFile("upload");*/
		
		if (fileSizeCheck(file)) {

			String oldFileName = file.getOriginalFilename();// 得到源文件名

			if (flieTypeCheck(oldFileName, type)) {

				// 获取参数
				String catalogId = request.getParameter("catalogId");
				String newsId = request.getParameter("newsId");
				String datefolder = DateUtils.getTime(new Date(), "yyyy-MM-dd");
				
								
				File rootFile=createPath(catalogId, newsId,datefolder);// 生成路径
				
				File newFile = generateNewFile(rootFile, oldFileName, type);//创建新文件				
				
				uploadFile(file,newFile);	// 上传文件			

				// 组装返回url，以便于ckeditor定位图片
				String fileUrl = ckeditorAccessImageUrl + "file/" + datefolder
						+ "/" + catalogId + "/" + newsId + "/" + File.separator
						+ newFile.getName();
				
				returnMeg(request, response, fileUrl);
			
			} else {
				String meg = "";
				if ("image".equals(type)) {
					meg = "',' 文件格式不正确（必须为.jpg/.jpeg/.gif/.bmp/.png文件）";
				} else if ("flash".equals(type)) {
					meg = "',' 文件格式不正确（必须为.avi/.gif/.swf/.flv文件）";
				}
				
				returnMeg(request, response, meg);
			}
		}else{
			String meg = "','文件过大";
			returnMeg(request, response, meg);
		}
	
		return "SUCCESS";
	}

	//上传文件
	private void uploadFile(MultipartFile file, File newFile) throws IOException {
		

		byte[] bytes = file.getBytes();
		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(newFile));
		stream.write(bytes);
		stream.close();
	}

	//校验文件类型
	private boolean flieTypeCheck(String fileName, String uploadType) {

		String fileType = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
		if ("image".equals(uploadType)) {
			for (String type : imageTypes) {
				if (type.equalsIgnoreCase(fileType)) {
					return true;
				}
			}
		} else if ("flash".equals(uploadType)) {
			for (String type : flashTypes) {
				if (type.equalsIgnoreCase(fileType)) {
					return true;
				}
			}
		}

		return false;
	}

	//生成上传路径
	private File createPath(String catalogId, String newsId,String datefolder) {
		
		String rootFilePath = ckeditorStorageImagePath + "/" + datefolder + "/"
				+ catalogId + "/" + newsId;

		// 创建目录
		File rootFile = new File(rootFilePath);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}

		return rootFile;
	}

	//校验文件大小
	private boolean fileSizeCheck(MultipartFile file) throws Exception{
		Long size = file.getSize();
		if (size > 0 && size < 1024 * 5120) {
			return true;
		}
		return false;
	}

	//生成新的文件
	private File generateNewFile(File directoryFile, String oldFileName,
			String type) {
		// 生成新的文件名
		String newFileName = "";
		if ("image".equals(type)) {
			newFileName = "IMAGE_" + DateUtils.getCurrentTime("HHmmss");
		} else if ("flash".equals(type)) {
			newFileName = "FLASH_" + DateUtils.getCurrentTime("HHmmss");
		}

		// 文件后缀
		String suffix = oldFileName.substring(oldFileName.lastIndexOf("."),
				oldFileName.length());

		// 创建新文件
		String pathName = directoryFile + "/" + File.separator + newFileName
				+ suffix;
		File newFile = new File(pathName);

		return newFile;

	}
	
	//返回给CKeditor页面信息
	private void returnMeg(HttpServletRequest request,HttpServletResponse response,String message) throws IOException{
		
		String callback = request.getParameter("CKEditorFuncNum");
		//返回给ckeditor
		String script = "<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction("
				+ callback + ", '" + message + "');</script>";

		PrintWriter out = response.getWriter();
		out.println(script);
		out.flush();
		out.close();
	}

}
