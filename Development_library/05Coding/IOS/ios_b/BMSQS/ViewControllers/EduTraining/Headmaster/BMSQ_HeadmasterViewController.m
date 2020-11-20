//
//  BMSQ_HeadmasterViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_HeadmasterViewController.h"
#import "VPImageCropperViewController.h"
#import "AFURLSessionManager.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#define ORIGINAL_MAX_WIDTH 640.0f
@interface BMSQ_HeadmasterViewController ()< UIImagePickerControllerDelegate,UINavigationControllerDelegate, VPImageCropperDelegate, UITextViewDelegate>

{
    UIImageView * fullImage;// 全图模式
    UIImageView * GraphicImage;// 图文模式
    UIImageView * textImage;//  文字模式
    int   options;// 顶部的选项卡  2:全图模式   3:图文模式    1:文字模式
    BOOL  isAdd;
    
    UIButton * fullBut;
    UIButton * GraphicBut;
    UIButton * textBut;
    
    
}

@property (nonatomic, strong)UITextView *EditorTextView;// 校长之语编辑
@property (nonatomic, strong)UIAlertController *alertController;// 上传图片的控制器
@property (nonatomic, strong)UITextView *actTextView; // 文字
@property (nonatomic, strong)NSString *txtContent;       //校长之语内容

@property (nonatomic, strong)NSMutableDictionary * dataSource;

@property (nonatomic, strong)UIView * topCellView;
@property (nonatomic, strong)UIImageView *imageView; //
@property (nonatomic, strong)UILabel * textViewPlaceholder; //
@property (nonatomic, strong)UILabel *label;
@property (nonatomic, strong)UILabel *label2;

@end

@implementation BMSQ_HeadmasterViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:@"校长之语"];
    [self setNavBackItem];
    isAdd = NO;
    options = 2;
    [self topOptionsView];
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
    
    
//    [self conentView];
//    [self setViewUp];
    [self getShopHeaderInfo];
    
}
#pragma mark ----- 右侧按钮
- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setTitle:@"保存" forState:UIControlStateNormal];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemEditorClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}
#pragma mark ------- 顶部的三个选项卡
- (void)topOptionsView
{
    
    self.topCellView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 30)];
    self.topCellView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.topCellView];
    
    fullImage = [[UIImageView alloc] initWithFrame:CGRectMake(6, 12, 14, 14)];
//    fullImage.backgroundColor = [UIColor blackColor];
    if (options == 2) {
        fullImage.image = [UIImage imageNamed:@"radio_yes"];
    }else{
        fullImage.image = [UIImage imageNamed:@"radio_no"];
    }
    
    fullImage.layer.cornerRadius = 7;
    [self.topCellView addSubview:fullImage];  // GraphicLB   textLB
    
    fullBut = [UIButton buttonWithType:UIButtonTypeCustom];
    fullBut.frame = CGRectMake(21, 3, 70, 32);
    [fullBut setTitle:@"全图模式" forState:UIControlStateNormal];
    [fullBut addTarget:self action:@selector(fullButClick:) forControlEvents:UIControlEventTouchUpInside];
    if (options == 2){
        [fullBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [fullBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    fullBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [self.topCellView addSubview:fullBut];
    
    GraphicImage = [[UIImageView alloc] initWithFrame:CGRectMake(111, 12, 14, 14)];
//    GraphicImage.backgroundColor = [UIColor blackColor];
    if (options == 3) {
        GraphicImage.image = [UIImage imageNamed:@"radio_yes"];
    }else{
        GraphicImage.image = [UIImage imageNamed:@"radio_no"];
    }
    GraphicImage.layer.cornerRadius = 7;
    [self.topCellView addSubview:GraphicImage];
    
    GraphicBut = [UIButton buttonWithType:UIButtonTypeCustom];
    GraphicBut.frame = CGRectMake(126, 3, 70, 32);
    [GraphicBut setTitle:@"图文模式" forState:UIControlStateNormal];
    [GraphicBut addTarget:self action:@selector(GraphicButClick:) forControlEvents:UIControlEventTouchUpInside];
    if (options == 3){
        [GraphicBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [GraphicBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    
    GraphicBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [self.topCellView addSubview:GraphicBut];
    
    textImage = [[UIImageView alloc] initWithFrame:CGRectMake(211, 12, 14, 14)];
//    textImage.backgroundColor = [UIColor blackColor];
    if (options == 1) {
        textImage.image = [UIImage imageNamed:@"radio_yes"];
    }else{
        textImage.image = [UIImage imageNamed:@"radio_no"];
    }
    textImage.layer.cornerRadius = 7;
    [self.topCellView addSubview:textImage];
    
    textBut = [UIButton buttonWithType:UIButtonTypeCustom];
    textBut.frame = CGRectMake(226, 3, 70, 32);
    [textBut setTitle:@"文字模式" forState:UIControlStateNormal];
    [textBut addTarget:self action:@selector(textButClick:) forControlEvents:UIControlEventTouchUpInside];
    if (options == 1){
        [textBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [textBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    
    textBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [self.topCellView addSubview:textBut];
    
}


- (void)conentView
{
     CGFloat cellHeight = APP_VIEW_WIDTH * (32.0/75.0);
    
    self.label = [[UILabel alloc ] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y +30 +cellHeight / 3, APP_VIEW_WIDTH, cellHeight/6)];
    self.label.textAlignment = NSTextAlignmentCenter;
    self.label.text = @"请上传照片";
    self.label.textColor = UICOLOR(102, 102, 102, 1);
    
    self.label2 = [[UILabel alloc ] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y +30 +cellHeight / 3 + cellHeight/6, APP_VIEW_WIDTH, cellHeight/6)];
    self.label2.font = [UIFont systemFontOfSize:13.f];
    self.label2.textAlignment = NSTextAlignmentCenter;
    self.label2.text = @"建议使用横向图片";
    self.label2.textColor = UICOLOR(102, 102, 102, 1);
    
    [self.view addSubview:self.label];
    [self.view addSubview:self.label2];
    
    self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 40, APP_VIEW_WIDTH, cellHeight)];
    self.imageView.backgroundColor = [UIColor clearColor];
    self.imageView.userInteractionEnabled = YES;
    
    if (self.dataSource[@"imgUrl"]) {
        
        [self.imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.dataSource[@"imgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }
    
    
    UITapGestureRecognizer*tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addPicture)];
    
    [self.imageView addGestureRecognizer:tapGesture];
    [self.view addSubview:self.imageView];
    
    
    
    self.actTextView = [[UITextView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 50 + cellHeight, APP_VIEW_WIDTH, 150)];
//    self.actTextView.layer.borderColor = [UIColor grayColor].CGColor;
//    self.actTextView.layer.borderWidth =1.0;
//    self.actTextView.layer.cornerRadius =5.0;
    self.actTextView.delegate = self;
    self.actTextView.backgroundColor = [UIColor whiteColor];
    if (self.dataSource[@"txtMemo"]) {
        self.actTextView.text = [NSString stringWithFormat:@"%@", self.dataSource[@"txtMemo"]];
        
    }
    
    UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
    [topView setBarStyle:UIBarStyleBlackTranslucent];
    
    UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(2, 5, 50, 25);
    [btn setTitle:@"隐藏" forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(dismissKeyBC) forControlEvents:UIControlEventTouchUpInside];
    [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
    UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
    NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
    [topView setItems:buttonsArray];
    [ self.actTextView setInputAccessoryView:topView];
    
    
    
    
    [self.view addSubview:self.actTextView];
    
    self.textViewPlaceholder = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 20)];
    self.textViewPlaceholder.font = [UIFont systemFontOfSize:13.f];
    self.textViewPlaceholder.textColor = UICOLOR(205, 205, 209, 1.0);
    self.textViewPlaceholder.text = @"请输入校长之语";
    if (self.dataSource[@"txtMemo"]){
        self.textViewPlaceholder.hidden = YES;
    }
    [self.actTextView addSubview:self.textViewPlaceholder];
    
}



- (void)setViewUp
{
    CGFloat cellHeight = APP_VIEW_WIDTH * (32.0/75.0);
    if (options == 2) {
        
        self.label.hidden = NO;
        self.label2.hidden = NO;
        self.imageView.hidden = NO;
        self.actTextView.hidden = YES;
        [fullBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        [GraphicBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        [textBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        fullImage.image = [UIImage imageNamed:@"radio_yes"];
        GraphicImage.image = [UIImage imageNamed:@"radio_no"];
        textImage.image = [UIImage imageNamed:@"radio_no"];
        
    }else if (options == 3){
        self.actTextView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y + 50 + cellHeight , APP_VIEW_WIDTH, 150);
        self.label.hidden = NO;
        self.label2.hidden = NO;
        self.imageView.hidden = NO;
        self.actTextView.hidden = NO;
        
        [fullBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        [GraphicBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        [textBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        
        self.textViewPlaceholder.text = @"请输入校长之语";
        fullImage.image = [UIImage imageNamed:@"radio_no"];
        GraphicImage.image = [UIImage imageNamed:@"radio_yes"];
        textImage.image = [UIImage imageNamed:@"radio_no"];
        
    }else if (options == 1){
        
        self.actTextView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y + 50, APP_VIEW_WIDTH, 150);
        self.actTextView.hidden = NO;
        self.label.hidden = YES;
        self.label2.hidden = YES;
        self.imageView.hidden = YES;
        
        [fullBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        [GraphicBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
        [textBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        
        fullImage.image = [UIImage imageNamed:@"radio_no"];
        GraphicImage.image = [UIImage imageNamed:@"radio_no"];
        textImage.image = [UIImage imageNamed:@"radio_yes"];

    }
    
}

#pragma mark ------ UITextViewDelegate

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    self.textViewPlaceholder.hidden = YES;
    if (options == 3) {
        self.topCellView.hidden = YES;
        self.actTextView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y + 50, APP_VIEW_WIDTH, 150);
        self.actTextView.hidden = NO;
        self.label.hidden = YES;
        self.label2.hidden = YES;
        self.imageView.hidden = YES;
    }
}


- (void)dismissKeyBC
{
    [self.actTextView resignFirstResponder];
    if (options == 3){
        CGFloat cellHeight = APP_VIEW_WIDTH * (32.0/75.0);
        self.actTextView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y + 50 + cellHeight , APP_VIEW_WIDTH, 150);
        self.textViewPlaceholder.hidden = YES;
        self.topCellView.hidden = NO;
        self.actTextView.hidden = NO;
        self.label.hidden = NO;
        self.label2.hidden = NO;
        self.imageView.hidden = NO;
    }
    
}


#pragma mark ------ 保存
- (void)itemEditorClick:(UIButton *)sender
{
    [self editShopHeader];
}
#pragma mark ------ 全图模式
- (void)fullButClick:(UIButton *)sender
{
    options = 2;
    [self setViewUp];
}
#pragma mark ------ 图文模式
- (void)GraphicButClick:(UIButton *)sender
{
    options = 3;
    [self setViewUp];
}
#pragma mark ------ 文字模式
- (void)textButClick:(UIButton *)sender
{
    options = 1;
    [self setViewUp];
}

#pragma mark ------  点击添加图片
- (void)addPicture
{
    [self presentViewController:self.alertController animated:YES completion:nil];
}


#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, (APP_VIEW_HEIGHT-self.view.frame.size.width*32.0/75)/2, self.view.frame.size.width, self.view.frame.size.width*32.0/75) limitScaleRatio:3.0];
        imgCropperVC.delegate = self;
        [self presentViewController:imgCropperVC animated:YES completion:^{
            // TO DO
        }];
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
- (void)imageCropper:(VPImageCropperViewController *)cropperViewController didFinished:(UIImage *)editedImage {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
        [self httpImageRequest:editedImage];
        
    }];
}

- (void)imageCropperDidCancel:(VPImageCropperViewController *)cropperViewController {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
    }];
}

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
                
                [self.dataSource setObject:code forKey:@"imgUrl"];
                [self.imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,self.dataSource[@"imgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
                
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

#pragma mark ------- getShopHeaderInfo  获取商家校长之语
- (void)getShopHeaderInfo
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];

    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopHeaderInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopHeaderInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
        
            self.dataSource = [NSMutableDictionary dictionaryWithDictionary:responseObject];
            
        }else{
            isAdd = YES;
            self.dataSource = [NSMutableDictionary dictionaryWithCapacity:0];
        }
        
        if (self.dataSource[@"expModel"]) {
            options = [self.dataSource[@"expModel"] intValue];
        }
        
        
        [self conentView];
        [self setViewUp];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark ----- editShopHeader  增加或修改校长之语
- (void)editShopHeader
{
    [SVProgressHUD showWithStatus:@""];
    
    if ([self.actTextView.text length] != 0) {
        [self.dataSource setObject:self.actTextView.text forKey:@"txtMemo"];
    }
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    NSString * headerCode = @"";
    if (self.dataSource[@"headerCode"]) {
        headerCode = self.dataSource[@"headerCode"];
    }
    
    [params setObject:headerCode forKey:@"headerCode"];
    
     NSMutableDictionary * newDic = [[NSMutableDictionary alloc]init];
    
    [newDic setObject:[NSString stringWithFormat:@"%d", options] forKey:@"expModel"];
    if (options == 1) {
        [newDic setObject:self.dataSource[@"txtMemo"] forKey:@"txtMemo"];
    }else if (options == 2){
        [newDic setObject:self.dataSource[@"imgUrl"] forKey:@"imgUrl"];
    }else if (options == 3){
        [newDic setObject:self.dataSource[@"txtMemo"] forKey:@"txtMemo"];
        [newDic setObject:self.dataSource[@"imgUrl"] forKey:@"imgUrl"];
    }
    
    if (isAdd) {
        [newDic setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    }
    
    
    NSString * updateData = [self jsonStringWithDictionary:newDic];
    
    [params setObject:updateData forKey:@"updateData"];
    
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"editShopHeader" strParams:headerCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"editShopHeader" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50000) {
            CSAlert(@"成功");
        }else if (code.intValue == 20000){
            CSAlert(@"失败");
        }else if (code.intValue == 50317){
            CSAlert(@"商家编码不能为空");
        }else if (code.intValue == 77025){
            CSAlert(@"表述模式为空");
        }else if (code.intValue == 77026){
            CSAlert(@"文字为空");
        }else if (code.intValue == 77027){
            CSAlert(@"图片为空");
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
