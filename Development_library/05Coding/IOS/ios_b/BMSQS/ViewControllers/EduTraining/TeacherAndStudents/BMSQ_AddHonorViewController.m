//
//  BMSQ_AddHonorViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/7.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_AddHonorViewController.h"
#import "VPImageCropperViewController.h"
#import "AFURLSessionManager.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "SVProgressHUD.h"
#import "BMSQ_StarTeacherViewController.h"
#import "BMSQ_TeacherDetailViewController.h"
#define ORIGINAL_MAX_WIDTH 640.0f
@interface BMSQ_AddHonorViewController ()<UITableViewDataSource, UITableViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate> //VPImageCropperDelegate

{
    int addNum;
}

@property (nonatomic, strong)UITableView * tableView;
@property (nonatomic, strong)UIAlertController *alertController;// 上传图片的控制器
@property (nonatomic, strong)NSMutableArray * dataSource;

@property (nonatomic, assign)int   row;

@end

@implementation BMSQ_AddHonorViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    if (self.number == 1) {
        [self setNavTitle:@"名师堂"];
        self.dataSource = [NSMutableArray arrayWithArray:self.teacherWork];
    }else if (self.number == 2){
        [self setNavTitle:@"学员之星"];
        self.dataSource = [NSMutableArray arrayWithArray:self.starWork];
    }
    [self setNavBackItem];
    [self customRightBtn];
    
    
    self.alertController = [UIAlertController alertControllerWithTitle:nil
                                                               message:nil
                                                        preferredStyle:UIAlertControllerStyleActionSheet];
    UIAlertAction* cancelAction = [UIAlertAction actionWithTitle:@"取消"
                                                           style:UIAlertActionStyleCancel
                                                         handler:nil];
    
    UIAlertAction* fromPhotoAction = [UIAlertAction actionWithTitle:@"从相册选择"
                                                              style:UIAlertActionStyleDefault
                                                            handler:^(UIAlertAction * action) {
                                                                
                                                                UIImagePickerController *controller = [[UIImagePickerController alloc] init];
                                                                controller.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                                                                NSMutableArray *mediaTypes = [[NSMutableArray alloc] init];
                                                                [mediaTypes addObject:(__bridge NSString *)kUTTypeImage];
                                                                controller.mediaTypes = mediaTypes;
                                                                controller.delegate = self;
                                                                [self presentViewController:controller
                                                                                   animated:YES
                                                                                 completion:^(void){
                                                                                     NSLog(@"Picker View Controller is presented");
                                                                                 }];
                                                                
                                                                
                                                            }];
    
    [self.alertController addAction:cancelAction];
    [self.alertController addAction:fromPhotoAction];
    

    
    [self setViewUp];
}
- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 50, 20, 48, 44);
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitle:@"保存" forState:UIControlStateNormal];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemsSaveClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)setViewUp
{
    
    UILabel * titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10,  APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH - 20, 15)];
    if (self.number == 1) {
        titleLB.text = @"所获荣誉作品";
    }else if (self.number == 2){
        titleLB.text = @"所获荣誉/作品/生活照";
    }
    
    titleLB.font = [UIFont systemFontOfSize:14.0];
    [self.view addSubview:titleLB];
    
//    UIButton * addBut = [UIButton buttonWithType:UIButtonTypeCustom];
//    addBut.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
//    [addBut addTarget:self action:@selector(addButsClick:) forControlEvents:UIControlEventTouchUpInside];
//    UIImageView * imageView = [[UIImageView alloc] initWithFrame:CGRectMake(addBut.frame.size.width / 2 - 20, 0, 40, addBut.frame.size.height)];
//    imageView.image = [UIImage imageNamed:@"right_add"];
//    [addBut addSubview:imageView];
//    addBut.backgroundColor = [UIColor grayColor];
    
    CGFloat cellHeight = self.view.frame.size.height/3;
    UIView * bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, cellHeight)];
    bottomView.backgroundColor = [UIColor whiteColor];
    
    UIImageView *cameraImage = [[UIImageView alloc ] initWithFrame:CGRectMake((APP_VIEW_WIDTH- 70)/2, cellHeight/3, 70, 70)];
    cameraImage.image = [UIImage imageNamed:@"camera"];
    
    UILabel *label2 = [[UILabel alloc ] initWithFrame:CGRectMake(0, cellHeight / 3 + 80, APP_VIEW_WIDTH, cellHeight/6)];
    label2.font = [UIFont systemFontOfSize:13.f];
    label2.textAlignment = NSTextAlignmentCenter;
    label2.text = @"上传照片";
    label2.textColor = [UIColor blackColor];// UICOLOR(102, 102, 102, 1)
    
    [bottomView addSubview:cameraImage];
    [bottomView addSubview:label2];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:bottomView.bounds];
    imageView.backgroundColor = [UIColor clearColor]; // APP_VIEW_BACKCOLOR
    imageView.userInteractionEnabled = YES;
    [bottomView addSubview:imageView];
    UITapGestureRecognizer*tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addHonorPicture)];
    
    [imageView addGestureRecognizer:tapGesture];
    
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 30, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 95) style:UITableViewStyleGrouped];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.tableFooterView = bottomView;
    [self.view addSubview:self.tableView];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataSource.count == 0) {
        return 0;
    }else{
        return self.dataSource.count;
    }
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *identify = @"activityTopImagesdf";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
    CGFloat height = 180.0;
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.contentView.backgroundColor = UICOLOR(230, 230, 230, 1);
        
        
//        CGFloat height = 150.0;
        
        UILabel *label = [[UILabel alloc ] initWithFrame:CGRectMake(0, height / 3, APP_VIEW_WIDTH, height/6)];
        label.textAlignment = NSTextAlignmentCenter;
        label.text = @"上传封面照片";
        label.textColor = UICOLOR(102, 102, 102, 1);
        
        
        
        UILabel *label2 = [[UILabel alloc ] initWithFrame:CGRectMake(0, height / 3 + height/6, APP_VIEW_WIDTH, height/6)];
        label2.font = [UIFont systemFontOfSize:13.f];
        label2.textAlignment = NSTextAlignmentCenter;
        label2.text = @"建议使用横向图片";
        label2.textColor = UICOLOR(102, 102, 102, 1);
        
        [cell.contentView addSubview:label];
        [cell.contentView addSubview:label2];
        
        
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, height)];
        imageView.backgroundColor = [UIColor clearColor];
        imageView.tag = 9001;
        [cell.contentView addSubview:imageView];
        
        
    }
    
    UIImageView *imageView = [cell.contentView viewWithTag:9001];
    NSString * url;
    if (self.number == 1) {
        url = self.dataSource[indexPath.row][@"workUrl"];
    }else if (self.number == 2){
        url = self.dataSource[indexPath.row][@"starImgUrl"];
    }
    
    
    [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, url]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    
    
    
    
    return cell;

}

//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    [self presentViewController:self.alertController animated:YES completion:nil];
//     self.row = (int)indexPath.row;
//    
//}



- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.dataSource removeObjectAtIndex:indexPath.row];
    
    [tableView  deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationRight];
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 185;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1.0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}
#pragma mark ------ 保存
- (void)itemsSaveClick:(UIButton *)sender
{
    if (self.dataSource.count == 0) {
        CSAlert(@"请上传荣誉或作品");
        
    }else{
        if (self.number == 1) {
            [self editShopTeacher];
        }else if (self.number == 2){
            [self editStudentStar];
        }
        
    }
    
    
}

#pragma mark -----  addHonorPicture
- (void)addHonorPicture
{
    [self presentViewController:self.alertController animated:YES completion:nil];
}


#pragma mark ------ 添加图片
//- (void)addButsClick:(UIButton *)sender
//{
//    NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithCapacity:0];
//    if (self.number == 1) {
//        [dic setObject:@"" forKey:@"workCode"];
//        [dic setObject:@"" forKey:@"workUrl"];
//    }else if (self.number == 2){
//        [dic setObject:@"" forKey:@"starWorkCode"];
//        [dic setObject:@"" forKey:@"starImgUrl"];
//
//    }
//    
//    [self.dataSource addObject:dic];
//    [self.tableView reloadData];
//}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        [self httpImageRequest:portraitImg];
//        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, (APP_VIEW_HEIGHT - 150)/2, self.view.frame.size.width, 150) limitScaleRatio:3.0];
//        imgCropperVC.delegate = self;
//        [self presentViewController:imgCropperVC animated:YES completion:^{
//            // TO DO
//        }];
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:^(){
    }];
}

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    // bug fixes: UIIMagePickerController使用中偷换StatusBar颜色的问题
    if ([navigationController isKindOfClass:[UIImagePickerController class]] &&
        ((UIImagePickerController *)navigationController).sourceType ==     UIImagePickerControllerSourceTypePhotoLibrary) {
        [[UIApplication sharedApplication] setStatusBarHidden:NO];
        [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];
    }
}




#pragma mark VPImageCropperDelegate
//- (void)imageCropper:(VPImageCropperViewController *)cropperViewController didFinished:(UIImage *)editedImage {
//    [cropperViewController dismissViewControllerAnimated:YES completion:^{
//        [self httpImageRequest:editedImage];
//        
//    }];
//}
//
//- (void)imageCropperDidCancel:(VPImageCropperViewController *)cropperViewController {
//    [cropperViewController dismissViewControllerAnimated:YES completion:^{
//    }];
//}

- (void)httpImageRequest:(UIImage *)image  {
    [SVProgressHUD showWithStatus:@"正在上传图片"];
    NSData *dataObj = UIImageJPEGRepresentation(image, 1);
    //    NSLog(@"%lu", (long)[dataObj length] / 1000) ;
    
    
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL ] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:dataObj name:@"imagefile" fileName:@"Icon.png" mimeType:@"image/png"];
        
        
    } error:nil];
    
    NSProgress *progressData = [NSProgress progressWithTotalUnitCount:dataObj.length];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    AFHTTPResponseSerializer *aa= [AFJSONResponseSerializer serializer];
    aa.acceptableContentTypes = [NSSet setWithObjects:@"application/json",@"text/json",@"text/javascript",@"text/html",@"text/plain",nil];
    manager.responseSerializer = aa;
    
    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:&progressData completionHandler:^(NSURLResponse *response, id responseObject, NSError *error) {
        if (error) {
            NSString *aString = [[NSString alloc] initWithData:[error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"] encoding:NSUTF8StringEncoding];
            CSAlert(aString);
            [SVProgressHUD dismiss];
            
        }else {
            
            NSString *code = [responseObject objectForKey:@"code"];
            if (code.length>5){
                NSLog(@"imgUrl ----> %@", code);
                NSLog(@"%@", self.dataSource);
                NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithCapacity:0];
                if (self.number == 1) {
                    [dic setObject:code forKey:@"workUrl"];
                }else if (self.number == 2){
                    [dic setObject:code forKey:@"starImgUrl"];
                }
                
                [self.dataSource addObject:dic];
                [self.tableView reloadData];
            }else if ([code isEqualToString:@"80020"]) {
                CSAlert(@"图片格式不正确");
            }else if ([code isEqualToString:@"80021"]) {
                CSAlert(@"图片大小不正确");
            }
            [SVProgressHUD dismiss];
        }
    }];
    
    [uploadTask resume];
    
}

#pragma mark image scale utility
- (UIImage *)imageByScalingToMaxSize:(UIImage *)sourceImage {
    if (sourceImage.size.width < ORIGINAL_MAX_WIDTH) return sourceImage;
    CGFloat btWidth = 0.0f;
    CGFloat btHeight = 0.0f;
    if (sourceImage.size.width > sourceImage.size.height) {
        btHeight = ORIGINAL_MAX_WIDTH;
        btWidth = sourceImage.size.width * (ORIGINAL_MAX_WIDTH / sourceImage.size.height);
    } else {
        btWidth = ORIGINAL_MAX_WIDTH;
        btHeight = sourceImage.size.height * (ORIGINAL_MAX_WIDTH / sourceImage.size.width);
    }
    CGSize targetSize = CGSizeMake(btWidth, btHeight);
    return [self imageByScalingAndCroppingForSourceImage:sourceImage targetSize:targetSize];
}


- (UIImage *)imageByScalingAndCroppingForSourceImage:(UIImage *)sourceImage targetSize:(CGSize)targetSize {
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = targetSize.width;
    CGFloat targetHeight = targetSize.height;
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0,0.0);
    if (CGSizeEqualToSize(imageSize, targetSize) == NO)
    {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if (widthFactor > heightFactor)
            scaleFactor = widthFactor; // scale to fit height
        else
            scaleFactor = heightFactor; // scale to fit width
        scaledWidth  = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        
        // center the image
        if (widthFactor > heightFactor)
        {
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
        }
        else
            if (widthFactor < heightFactor)
            {
                thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
            }
    }
    UIGraphicsBeginImageContext(targetSize); // this will crop
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width  = scaledWidth;
    thumbnailRect.size.height = scaledHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    if(newImage == nil) NSLog(@"could not scale image");
    
    //pop the context to get back to the default
    UIGraphicsEndImageContext();
    return newImage;
}

#pragma mark ------ editShopTeacher 增加或修改名师信息
- (void)editShopTeacher
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *teachCodeStr = @"";
    if (self.teacherCode.length >0) {
        teachCodeStr = self.teacherCode;
    }
    [params setObject:teachCodeStr forKey:@"teacherCode"];

    NSMutableDictionary * newDic = [[NSMutableDictionary alloc]init];

    if (self.teacherCode == nil) {
        [newDic setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    }

    [newDic setObject:self.teachCourse forKey:@"teachCourse"];
    [newDic setObject:self.teacherImgUrl forKey:@"teacherImgUrl"];
    [newDic setObject:self.teacherInfo forKey:@"teacherInfo"];
    [newDic setObject:self.teacherName forKey:@"teacherName"];
    [newDic setObject:self.teacherTitle forKey:@"teacherTitle"];
    [newDic setObject:self.dataSource forKey:@"teacherWork"];
    
    NSString * updateData = [self jsonStringWithDictionary:newDic];
    
    [params setObject:updateData forKey:@"updateData"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"editShopTeacher" strParams:teachCodeStr];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"editShopTeacher" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50000) {
            CSAlert(@"成功");
            [[NSNotificationCenter defaultCenter] postNotificationName:@"StarTeacherList" object: nil];
            BMSQ_StarTeacherViewController * starVC = self.navigationController.viewControllers[1];
            [self.navigationController popToViewController:starVC animated:YES];
        }else if (code.intValue == 20000){
            CSAlert(@"失败");
        }else if (code.intValue == 50317){
            CSAlert(@"商家编码不能为空");
        }else if (code.intValue == 77017){
            CSAlert(@"教师名字不能为空");
        }else if (code.intValue == 77018){
            CSAlert(@"教师职称不能为空");
        }

        

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark ---------  增加或修改学院之星
- (void)editStudentStar
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *starCodeStr = @"";
    if (self.starCode.length >0) {
        starCodeStr = self.starCode;
    }
    [params setObject:starCodeStr forKey:@"starCode"];
    
    NSMutableDictionary * newDic = [[NSMutableDictionary alloc]init];
    
    if (self.starCode == nil) {
        [newDic setObject:self.signCode forKey:@"signCode"];
    }
    
    [newDic setObject:self.starInfo forKey:@"starInfo"];
    [newDic setObject:self.starName forKey:@"starName"];
    [newDic setObject:self.starUrl forKey:@"starUrl"];
    [newDic setObject:self.dataSource forKey:@"starWork"];
    
    NSString * updateData = [self jsonStringWithDictionary:newDic];
    
    [params setObject:updateData forKey:@"updateData"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"editStudentStar" strParams:starCodeStr];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"editStudentStar" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50000) {
            CSAlert(@"成功");
            
            [[NSNotificationCenter defaultCenter] postNotificationName:@"teacherDetailList" object: nil];
            
            BMSQ_TeacherDetailViewController * VC = self.navigationController.viewControllers[1];
            [self.navigationController popToViewController:VC animated:YES];
        }else if (code.intValue == 20000){
            CSAlert(@"失败");
        }else if (code.intValue == 50317){
            CSAlert(@"商家编码不能为空");
        }else if (code.intValue == 77019){
            CSAlert(@"报名编码为空");
        }else if (code.intValue == 77021){
            CSAlert(@"活动名称为空");
        }
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];
    
}




- (NSString *)jsonStringWithDictionary:(NSMutableDictionary *)dic {
    NSString *jsonString ;
    if (dic.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
            //            NSLog(@"json解析失败：%@",err);
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}



#pragma mark ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
