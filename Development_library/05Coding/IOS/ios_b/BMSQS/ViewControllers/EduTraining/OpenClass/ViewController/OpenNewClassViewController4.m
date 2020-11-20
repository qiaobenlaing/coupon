//
//  OpenNewClassViewController4.m
//  BMSQS
//
//  Created by gh on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassViewController4.h"
#import "OpenClassViewController.h"
#import "AFURLSessionManager.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"


#define ORIGINAL_MAX_WIDTH 640.0f
@interface OpenNewClassViewController4 ()<UIImagePickerControllerDelegate,UINavigationControllerDelegate>

@property (nonatomic, strong)UIAlertController *alertController;// 上传图片的控制器
@property (nonatomic, strong)NSString * imageUrl;

@end

@implementation OpenNewClassViewController4

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    
}

- (void)setViewUp {
    [self setNavBackItem];
    [self setNavTitle:@"开班"];
    [self setRightBtn];
    
    
    self.imageUrl = @"";

    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y+5, APP_VIEW_WIDTH-10, 40)];
    label.text = @"课程形象照";
    label.font = [UIFont systemFontOfSize:16.f];
    [self.view addSubview:label];
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+5+40, APP_VIEW_WIDTH, 100)];
    imageView.userInteractionEnabled = YES;
    imageView.tag = 2001;
    imageView.backgroundColor = [UIColor orangeColor];
    [self.view addSubview:imageView];
    
    UITapGestureRecognizer*tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addPersonPicture)];
    
    [imageView addGestureRecognizer:tapGesture];
    
    [self setMyAlertView];
    
    
    
    
}

- (void)setMyAlertView {
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
}


#pragma mark - button 点击事件
- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 1000) { //
//        if (self.imageUrl.length == 0) {
//            CSAlert(@"请上传图片");
//            return;
//        }
        self.imageUrl =  @"/Public/Uploads/20160318/56eb51b356700.png";
        
        [self addShopClass];
        
    }
    
    
}

// 完成
- (void)setRightBtn {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-74, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + APP_STATUSBAR_HEIGHT, 64, APP_NAV_LEFT_ITEM_HEIGHT);
    button.tag = 1000;
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"完成" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [button.titleLabel setTextAlignment:NSTextAlignmentRight];
    [self setRight:button];
    
    
}

- (void)addShopClass {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [NSMutableDictionary dictionaryWithDictionary:self.requestDic];
    
    [params setObject:self.imageUrl forKey:@"classUrl"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];

    NSString* vcode = [gloabFunction getSign:@"addShopClass" strParams:[self.requestDic objectForKey:@"className"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"addShopClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"%@",responseObject);
        
        NSString *code = [responseObject objectForKey:@"code"];
        if (code.intValue == 50000) {
            OpenClassViewController * vc = self.navigationController.viewControllers[1];
            [self.navigationController popToViewController:vc animated:YES];
            NSNotification * notice = [NSNotification notificationWithName:@"OpenNewClassViewControllerIsFinish" object:nil];
            //发送消息
            [[NSNotificationCenter defaultCenter]postNotification:notice];
        }
        else {
            CSAlert([self errorManger:code]);
        }
        
        
       
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];
}

//50317-商家编码不能为空；77001-上课时间为空； 77002-教师编码为空； 77003-班级名字为空； 77004-学习开始时间为空； 77005-学习结束时间为空； 77006-适合描述为空； 77007-报名费用为空； 77008-所学课时为空； 77009-报名开始时间为空； 77010-报名结束时间为空；77012-周几为空； 77013-上课开始时间为空； 77014-上课结束时间为空；
- (NSString *)errorManger:(NSString *)code {
    NSString *mangerString;
    switch (code.intValue) {
        case 20000:
            mangerString = @"失败";
            break;
        case 50317:
            mangerString = @"商家编码不能为空";
            break;
        case 77001:
            mangerString = @"上课时间为空";
            break;
        case 77002:
            mangerString = @"教师编码为空";
            break;
        case 77003:
            mangerString = @"班级名字为空";
            break;
        case 77004:
            mangerString = @"学习开始时间为空";
            break;
        case 77005:
            mangerString = @"学习结束时间为空";
            break;
        case 77006:
            mangerString = @"适合描述为空";
            break;
        case 77007:
            mangerString = @"报名费用为空";
            break;
        case 77008:
            mangerString = @"所学课时为空";
            break;
        case 77009:
            mangerString = @"报名开始时间为空";
            break;
        case 77010:
            mangerString = @"报名结束时间为空";
            break;
        case 77012:
            mangerString = @"周几为空";
            break;
        case 77013:
            mangerString = @"上课开始时间为空";
            break;
        case 77014:
            mangerString = @"上课结束时间为空";
            break;
        default:
            mangerString = code;
            break;
    }
    return mangerString;
    
}



#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        [self httpImageRequest:portraitImg];
        
        
        
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

- (void)httpImageRequest:(UIImage *)image  {
    [SVProgressHUD showWithStatus:@"正在上传图片"];
    
    UIImageView *imageView = [self.view viewWithTag:2001];
    [imageView setImage:image];
    
    NSData *dataObj = UIImageJPEGRepresentation(image, 0.0001);
    
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
                self.imageUrl = code;

                
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



#pragma mark ------  上传图片
- (void)addPersonPicture
{
    [self presentViewController:self.alertController animated:YES completion:nil];
}


@end
