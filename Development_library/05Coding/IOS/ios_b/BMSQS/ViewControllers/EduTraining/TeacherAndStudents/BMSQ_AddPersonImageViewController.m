//
//  BMSQ_AddPersonImageViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/7.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_AddPersonImageViewController.h"
#import "VPImageCropperViewController.h"
#import "AFURLSessionManager.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_AddIntroduceViewController.h"
#define ORIGINAL_MAX_WIDTH 640.0f
@interface BMSQ_AddPersonImageViewController ()<UIImagePickerControllerDelegate,UINavigationControllerDelegate> // VPImageCropperDelegate

@property (nonatomic, strong)UIAlertController *alertController;// 上传图片的控制器
@property (nonatomic, strong)NSString * pictureUrl;

@end

@implementation BMSQ_AddPersonImageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
//    self.view.backgroundColor = [UIColor whiteColor];
    if (self.number == 1) {
        [self setNavTitle:@"名师堂"];
    }else if (self.number == 2){
        [self setNavTitle:@"学员之星"];
    }
    
    [self setNavBackItem];
    [self customRightBtn];
    
    if (self.number == 1) {
        self.pictureUrl = self.teacherImgUrl;
    }else if (self.number == 2){
        self.pictureUrl = self.starUrl;
    }
    
    
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
    [item setTitle:@"下一步" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemNextClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)setViewUp
{
    
    
    UILabel * titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10,  APP_VIEW_ORIGIN_Y + 10, 100, 15)];
    titleLB.textColor = UICOLOR(102, 102, 102, 1.0);
    titleLB.text = @"上传一张形象照";
    titleLB.font = [UIFont systemFontOfSize:14.0];
    [self.view addSubview:titleLB];
    
    CGFloat cellHeight = self.view.frame.size.height/3;
    UIView * topView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 30, APP_VIEW_WIDTH, cellHeight)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    UIImageView *cameraImage = [[UIImageView alloc ] initWithFrame:CGRectMake((APP_VIEW_WIDTH- 70)/2, cellHeight/3, 70, 70)];
    cameraImage.image = [UIImage imageNamed:@"camera"];
    
    UILabel *label2 = [[UILabel alloc ] initWithFrame:CGRectMake(0, cellHeight / 3 + 80, APP_VIEW_WIDTH, cellHeight/6)];
    label2.font = [UIFont systemFontOfSize:13.f];
    label2.textAlignment = NSTextAlignmentCenter;
    label2.text = @"上传照片";
    label2.textColor = [UIColor blackColor];// UICOLOR(102, 102, 102, 1)
    
    [topView addSubview:cameraImage];
    [topView addSubview:label2];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:topView.bounds];
    imageView.backgroundColor = [UIColor clearColor]; // APP_VIEW_BACKCOLOR
    imageView.userInteractionEnabled = YES;
    imageView.tag = 10001;
    
    if (self.number == 1) {
        
        if (self.teacherImgUrl != nil && ![self.teacherImgUrl isKindOfClass:[NSNull class]] && [self.teacherImgUrl length] != 0) {
            
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.teacherImgUrl ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        }
        
    }else if (self.number == 2){
        
        if (self.starUrl != nil && ![self.starUrl isKindOfClass:[NSNull class]] && [self.starUrl length] != 0) {
            
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.starUrl ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
        }
        
    }
    
    
    
    
    
    UITapGestureRecognizer*tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addPersonPicture)];
    
    [imageView addGestureRecognizer:tapGesture];
    [topView addSubview:imageView];
}


#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        [self httpImageRequest:portraitImg];
//        portraitImg = [self imageByScalingToMaxSize:portraitImg];
//        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, (APP_VIEW_HEIGHT-self.view.frame.size.height/3)/2, self.view.frame.size.width, self.view.frame.size.height/3) limitScaleRatio:3.0];
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
                self.pictureUrl = code;
                UIImageView * iconImage = [self.view viewWithTag:10001];
                [iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.pictureUrl ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
                
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





#pragma mark ------ 下一步
- (void)itemNextClick:(UIButton *)sender
{
    
    if (self.pictureUrl == nil) {
        CSAlert(@"请上传形象照");
    }else{
        
         BMSQ_AddIntroduceViewController * VC = [[BMSQ_AddIntroduceViewController alloc] init];
        
        if (self.number == 1){
            
            VC.teacherName = self.teacherName;
            VC.teacherTitle = self.teacherTitle;
            VC.teachCourse = self.teachCourse;
            VC.teacherImgUrl = self.pictureUrl;
            VC.teacherInfo = self.teacherInfo;
            VC.teacherWork = self.teacherWork;
            VC.teacherCode = self.teacherCode;
            VC.number = 1;
            
        }else if (self.number == 2){
            
            VC.starName = self.starName;
            VC.starCode = self.starCode;
            VC.signCode = self.signCode;
            VC.starInfo = self.starInfo;
            VC.starUrl = self.pictureUrl;
            VC.starWork = self.starWork;
            VC.number = 2;
            
        }
        [self.navigationController pushViewController:VC animated:YES];
        
    }
    
    
    
}

#pragma mark ------  上传图片
- (void)addPersonPicture
{
    [self presentViewController:self.alertController animated:YES completion:nil];
}

#pragma mark ------ 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
