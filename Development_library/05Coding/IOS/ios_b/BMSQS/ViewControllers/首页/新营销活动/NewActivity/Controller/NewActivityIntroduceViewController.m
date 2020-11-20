//
//  NewActivityIntroduceViewController.m
//  BMSQS
//
//  Created by gh on 16/1/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "NewActivityIntroduceViewController.h"
#import "YYText.h"

#import "DAProgressOverlayView.h"
#import "AFURLSessionManager.h"

#import "ActImageTextAttachment.h"
#import "NSAttributedString+EmojiExtension.h"
#import "SVProgressHUD.h"

#define ORIGINAL_MAX_WIDTH 640.0f

@interface NewActivityIntroduceViewController () <UITextViewDelegate, YYTextViewDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate>

//@property (nonatomic, strong)YYTextView *textView;
@property (nonatomic, strong)UITextView *textView;

@property (nonatomic, copy)NSString *textStr;


@end



@implementation NewActivityIntroduceViewController

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification
                                                  object:nil];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"活动介绍"];
    [self setNavBackItem];
    [self setRightBtn];
    [self setViewUp];
    
    
}

- (NSMutableAttributedString *)actAttributedWithString:(NSString *)str {
    NSArray *array = [self arrayWithTxtContent:str];
    
    NSMutableAttributedString * mutStr = [NSMutableAttributedString new];
    for (NSString *txtStr in array) {
        if([txtStr hasPrefix:@"/Public/Uploads/"]) {
            //            [NSURL URLWithString:]
            NSURL *url = [NSURL URLWithString: [NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, txtStr]];
            UIImage *imagea;
            imagea = [UIImage imageWithData: [NSData dataWithContentsOfURL:url]];
            imagea = [self imageCompressForWidth:imagea targetWidth:APP_VIEW_WIDTH-10];
            ActImageTextAttachment * attachment1 = [ActImageTextAttachment new] ;
            attachment1.imgUrl = [NSString stringWithFormat:@"<img src=\"%@\" /><BR />",txtStr];
            attachment1.imgSize = CGSizeMake(imagea.size.width, imagea.size.height);
            attachment1.image = imagea;
            NSAttributedString * attachStr1 = [NSAttributedString attributedStringWithAttachment:attachment1];
            [mutStr appendAttributedString:attachStr1];
        }else {
            [mutStr appendAttributedString:[[NSAttributedString alloc] initWithString:txtStr attributes:nil]];
            
            
        }
        
    }
    
    return mutStr;


    
}

#pragma mark - 将txtcontent字符串转换为数组txtContent
- (NSArray *)arrayWithTxtContent:(NSString *)string {
    NSMutableString *mutableString = [[NSMutableString alloc] initWithString:string];
    while ([mutableString rangeOfString:@"<img src="].length > 0 || [mutableString rangeOfString:@"\"/><BR />"].length > 0) {
        
        [mutableString deleteCharactersInRange:[mutableString rangeOfString:@"<img src="]];
        [mutableString deleteCharactersInRange:[mutableString rangeOfString:@"/><BR />"]];
        NSLog(@"%@",mutableString);
        
    }
    
    NSLog(@"%@",mutableString);
    
    NSArray *array = [mutableString componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"\""]]; //从字符A中分隔成2个元素的数组
    
    NSLog(@"%@",array);
    return array;
}


- (void)setViewUp {
    
//    YYTextView *textView= [YYTextView new];
    UITextView *textView = [[UITextView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
//    textView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y);
    textView.font = [UIFont systemFontOfSize:16.f];
    textView.textColor = [UIColor blackColor];
    textView.keyboardDismissMode = UIScrollViewKeyboardDismissModeInteractive;
//    textView.placeholderText = @"请输入活动详情";
    textView.delegate = self;
    [self.view addSubview:textView];
    self.textView = textView;
    
    [self setUpToolBar];
    
    //Add keyboard notification
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillShowNotification object:nil];
    
    
    if (self.txtContent) {
        self.textView.attributedText = [self actAttributedWithString:self.txtContent];
    }
    
    textView.selectedRange = NSMakeRange(textView.attributedText.length, 0);
    [textView becomeFirstResponder];
    
    
}

//完成
- (void)clickFinish:(UIButton *)btn {
    
    //http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg
    
    NSLog(@"%@", [NSString stringWithFormat:@"Output: %@", [_textView.textStorage getPlainString]]);
    
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    [dic setObject:[_textView.textStorage getPlainString] forKey:@"txtContent"];

    
    NSNotification *notification =[NSNotification notificationWithName:@"txtContentFinish"
                                                                object:nil
                                                              userInfo:dic];
    
    [[NSNotificationCenter defaultCenter] postNotification:notification];
    
    
    [self.navigationController popViewControllerAnimated:YES];
    
    /*
    [_textView.attributedText enumerateAttribute:YYTextAttachmentAttributeName
                                         inRange:NSMakeRange(0, _textView.attributedText.length)
                                         options:0
                                      usingBlock:^(id  _Nullable value, NSRange range, BOOL * _Nonnull stop) {
                                          
//                                         _textView.attributedText.
                                          NSLog(@"%@", value);
                                          
                                          
                                      }];
    
     */
}

- (void)textViewDidChange:(UITextView *)textView {
    
    NSLog(@"%@",textView.attributedText);
    
}


- (void)setRightBtn{

    UIButton* rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    rightBtn.frame = CGRectMake(APP_VIEW_WIDTH - 44, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    rightBtn.backgroundColor = [UIColor clearColor];
    [rightBtn addTarget:self action:@selector(clickFinish:) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.tag = 10001;
    rightBtn.titleEdgeInsets = UIEdgeInsetsMake(0, 0, 0, 5);
    [rightBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [rightBtn setTitle:@"保存" forState:UIControlStateNormal];
    [self.navigationView addSubview:rightBtn];
    
    
}

- (void)resetTextStyle {
    //After changing text selection, should reset style.
    NSRange wholeRange = NSMakeRange(0, _textView.textStorage.length);
    
    [_textView.textStorage removeAttribute:NSFontAttributeName range:wholeRange];
    
    [_textView.textStorage addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:22.0f] range:wholeRange];
    
    
}


- (void)btnAct{
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
    

    
}


#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageCompressForWidth:portraitImg targetWidth:APP_VIEW_WIDTH - 10];
        
        
        [self httpImageRequest:portraitImg];
        /*
//        NSData *dataObj = UIImageJPEGRepresentation(portraitImg, 0.00000001);
        NSMutableAttributedString *attachment = nil;
        attachment = [NSMutableAttributedString yy_attachmentStringWithContent:portraitImg
                                                                   contentMode:UIViewContentModeCenter
                                                                attachmentSize:portraitImg.size
                                                                   alignToFont:nil
                                                                     alignment:YYTextVerticalAlignmentCenter];
//        ActImageTextAttachment *actAttachment = (ActImageTextAttachment *)attachment;
//        attachment.img = @"adfasdfasdfasdf";
        
        NSMutableAttributedString *labelText = [_textView.attributedText mutableCopy];
        [labelText insertAttributedString:attachment atIndex:_textView.selectedRange.location];
        
        _textView.attributedText = labelText;
        */
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


#pragma mark - 上传图片 httpImageRequest

- (void)httpImageRequest:(UIImage *)image  {
    [SVProgressHUD showWithStatus:@"正在上传图片"];
    NSData *dataObj = UIImageJPEGRepresentation(image, 1);
    
    
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
        [SVProgressHUD dismiss];
        if (error) {
            NSString *aString = [[NSString alloc] initWithData:[error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"] encoding:NSUTF8StringEncoding];
            //            NSLog(@"Error: %@",aString );
//            [SVProgressHUD dismiss];
            
        }else {
            NSLog(@"%@",responseObject);
            NSString *code = [responseObject objectForKey:@"code"];
            if (code.length>5){
                //                code;
                //                NSLog(@"%@",responseObject);
                //                [self updateShopLogo:code];
                NSLog(@"imgUrl ----> %@", code);
//                _activityImg = code;
                
                ActImageTextAttachment *textAttachment = [ActImageTextAttachment new];
                
                //Set tag and image
                textAttachment.imgUrl = [NSString stringWithFormat:@"<img src=\"%@\" /><BR />",code];
                textAttachment.image = image;
                
                //Set emoji size
                textAttachment.imgSize = CGSizeMake(image.size.width,image.size.height);
                
                //Insert emoji image
                [_textView.textStorage insertAttributedString:[NSAttributedString attributedStringWithAttachment:textAttachment]
                                                      atIndex:_textView.selectedRange.location];
                
                //Move selection location
                _textView.selectedRange = NSMakeRange(_textView.selectedRange.location + 1, _textView.selectedRange.length);
                
                //Reset text style
                [self resetTextStyle];
                

                
            }else if ([code isEqualToString:@"80020"]) {
                CSAlert(@"图片格式不正确");
            }else if ([code isEqualToString:@"80021"]) {
                CSAlert(@"图片大小不正确");
            }

        }
    }];
    
    [uploadTask resume];
    
}


#pragma mark  UIToolbar//上传图片、完成按钮
- (void)setUpToolBar {
    UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
    [topView setBarStyle:UIBarStyleBlackOpaque];
    
    
    UIButton *btn2 = [UIButton buttonWithType:UIButtonTypeCustom];
    btn2.frame = CGRectMake(0, 5, APP_VIEW_WIDTH/2, 30);
    [btn2 setImage:[UIImage imageNamed:@"上传图片"] forState:UIControlStateNormal];
    btn2.imageEdgeInsets = UIEdgeInsetsMake(0, 5, 0, APP_VIEW_WIDTH/2-30);
    [btn2 addTarget:self action:@selector(btnAct) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithCustomView:btn2];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(0, 5, APP_VIEW_WIDTH/2, 25);
    [btn setTitle:@"完成" forState:UIControlStateNormal];
    btn.titleLabel.textAlignment = NSTextAlignmentRight;
    btn.titleLabel.backgroundColor = [UIColor clearColor];
    btn.titleEdgeInsets = UIEdgeInsetsMake(0, APP_VIEW_WIDTH/4, 0, 10);
    [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
    NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
    [topView setItems:buttonsArray];
    [_textView setInputAccessoryView:topView];
    [topView setItems:buttonsArray];
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

-(UIImage *) imageCompressForWidth:(UIImage *)sourceImage targetWidth:(CGFloat)defineWidth{
    
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = defineWidth;
    CGFloat targetHeight = height / (width / targetWidth);
    CGSize size = CGSizeMake(targetWidth, targetHeight);
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0, 0.0);
    
    if(CGSizeEqualToSize(imageSize, size) == NO){
        
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if(widthFactor > heightFactor){
            scaleFactor = widthFactor;
        }
        else{
            scaleFactor = heightFactor;
        }
        scaledWidth = width * scaleFactor;
        scaledHeight = height * scaleFactor;
        
        if(widthFactor > heightFactor){
            
            thumbnailPoint.y = (targetHeight - scaledHeight) * 0.5;
            
        }else if(widthFactor < heightFactor){
            
            thumbnailPoint.x = (targetWidth - scaledWidth) * 0.5;
        }
    }
    
    UIGraphicsBeginImageContext(size);
    
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width = scaledWidth;
    thumbnailRect.size.height = scaledHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    if(newImage == nil){
        
        NSLog(@"scale image fail");
    }
    UIGraphicsEndImageContext();
    return newImage;
}

#pragma mark --- 隐藏键盘
- (void)dismissKeyB {
    
    [self.textView resignFirstResponder];
    
    
}

#pragma mark - Keyboard notification

- (void)onKeyboardNotification:(NSNotification *)notification {
    //Reset constraint constant by keyboard height
    if ([notification.name isEqualToString:UIKeyboardWillShowNotification]) {
        CGRect keyboardFrame = ((NSValue *) notification.userInfo[UIKeyboardFrameEndUserInfoKey]).CGRectValue;
        
        _textView.frame = CGRectMake(_textView.frame.origin.x, _textView.frame.origin.y, _textView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-keyboardFrame.size.height);
        
    } else if ([notification.name isEqualToString:UIKeyboardWillHideNotification]) {
        _textView.frame = CGRectMake(_textView.frame.origin.x, _textView.frame.origin.y, _textView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    }
    
    //Animate change
    [UIView animateWithDuration:0.8f animations:^{
        [self.view layoutIfNeeded];
    }];
}




@end
