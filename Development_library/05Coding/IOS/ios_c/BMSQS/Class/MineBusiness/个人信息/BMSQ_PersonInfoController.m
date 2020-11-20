//
//  BMSQ_PersonInfoController.m
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PersonInfoController.h"
#import "BMSQ_PersonInfoCell.h"
#import "BMSQ_ModifyInfoController.h"
#import "BMSQ_ModifySignController.h"
#import "BMSQ_modifyPwdViewController.h"


@interface BMSQ_PersonInfoController ()
{
    UITableView* m_tableView;
    UITableView* m_tableViewCity;
    NSMutableArray* m_dataSourceCity;
    NSMutableDictionary* m_dicUserInfo;
    UIButton* btnRightBar;
    NSString* strNickName;
    NSString* strAvatarUrl;
    NSString* strName;
    NSString* strSex;
    NSString* strCity;
    NSString* strSign;
    UITextField* currentTextField;
    UIImageView* iv_logo;
}

@end

@implementation BMSQ_PersonInfoController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavTitle:@"个人信息"];
    [self setNavBackItem];
    
    [self setViewUp];
}

- (void)setViewUp
{
    
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 100, 25, 70, 70)];
    
    m_dicUserInfo = [[NSMutableDictionary alloc]init];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:m_tableView];
    
    m_dataSourceCity = [[NSMutableArray alloc]init];
    
    m_tableViewCity = [[UITableView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT - 230, APP_VIEW_WIDTH/2, 180)];
    m_tableViewCity.dataSource = self;
    m_tableViewCity.delegate = self;
    m_tableViewCity.hidden = YES;
    m_tableViewCity.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableViewCity];
    
    [self listZhejiangCity];
    
    currentTextField = [[UITextField alloc]init];
    currentTextField.returnKeyType = UIReturnKeyDone;
    currentTextField.delegate = self;
    
    btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    [btnRightBar setTitle:@"编辑" forState:UIControlStateNormal];
    [btnRightBar setTitle:@"提交" forState:UIControlStateSelected];
    [btnRightBar.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnRightBar addTarget:self action:@selector(btnRightClick) forControlEvents:UIControlEventTouchUpInside];
//    UIImageView* ivScan = [[UIImageView alloc]initWithFrame:CGRectMake(12, 15, 19, 14)];
//    ivScan.image = [UIImage imageNamed:@"iv_edite"];
    //[btnRightBar addSubview:ivScan];
    
    [self setNavRightBarItem:btnRightBar];
    
    [self getUserInfo];
}

- (void)btnRightClick
{
    btnRightBar.selected = !btnRightBar.selected;
    if (!btnRightBar.selected)
    {
        [self updateUserInfo];
    }
    [m_tableView reloadData];
}

- (void)getUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getUserInfo" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        m_dicUserInfo = [[NSMutableDictionary alloc]initWithDictionary:responseObject];
        strSign = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"signature"]];
        strSex = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"sex"]];
        strName = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"realName"]];
        strNickName = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"nickName"]];
        strCity = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"city"]];
        strAvatarUrl = [[NSString alloc]initWithFormat:@"%@",[m_dicUserInfo objectForKey:@"avatarUrl"]];
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (void)updateUserInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[m_dicUserInfo objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];
    NSDictionary* dicData = [[NSDictionary alloc]init];
    dicData = @{@"nickName":strNickName,@"realName":strName,@"city":strCity,@"signature":strSign,@"sex":strSex,@"avatarUrl":strAvatarUrl};
    [params setObject:dicData forKey:@"updateInfo"];
//    [params setObject:strNickName forKey:@"nickName"];
//    [params setObject:strName forKey:@"realName"];
//    [params setObject:strCity forKey:@"city"];
//    [params setObject:strSign forKey:@"signature"];
//    [params setObject:strSex forKey:@"sex"];
//    [params setObject:strAvatarUrl forKey:@"avatarUrl"];
    
    NSString* vcode = [gloabFunction getSign:@"updateUserInfo" strParams:[m_dicUserInfo objectForKey:@"mobileNbr"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updateUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [self.navigationController popViewControllerAnimated:YES];
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (m_tableViewCity == tableView)
    {
        return 0;
    }
    
    if(section == 0)
        return 0;
    return 20;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_FOTERVIEW_HEIGHT)];
    v.backgroundColor = APP_VIEW_BACKCOLOR;
    return v;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (tableView == m_tableViewCity)
    {
        return m_dataSourceCity.count;
    }
    else
    {
        if (section == 0)
        {
            return 1;
        }
        else if(section == 1)
        {
            return 5;
        }
        else
        {
            return 1;
        }
    }
  
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (tableView == m_tableViewCity)
    {
        return 44;
    }
    else
    {
        if (indexPath.section == 0)
        {
            return 120;
        }
        
        else
        {
            return 44;
        }
    }
  
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (tableView == m_tableViewCity)
    {
        static NSString* cellIdentify = @"cellIdentify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line0.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line0];
        }
        
        cell.textLabel.text = [[m_dataSourceCity objectAtIndex:indexPath.row]objectForKey:@"name"];
        return cell;
    }
    else
    {
        if (indexPath.section == 0)
        {
            static NSString* cellIdentify = @"cellIdentify";
            UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
            if (cell == nil)
            {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 119, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
                line0.backgroundColor = APP_CELL_LINE_COLOR;
                [cell addSubview:line0];
            }
            
            
            iv_logo.layer.cornerRadius = 35;
            iv_logo.layer.masksToBounds = YES;
            iv_logo.layer.borderWidth = 0.3;
            [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,strAvatarUrl]] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
            [cell addSubview:iv_logo];
            
            UILabel* lb_name = [[UILabel alloc]initWithFrame:CGRectMake(10, 50, 100, 20)];
            lb_name.backgroundColor = [UIColor clearColor];
            lb_name.textColor =  UICOLOR(36, 36, 36, 1.0);
            lb_name.font = [UIFont systemFontOfSize:14];
            lb_name.text = @"头像";
            [cell addSubview:lb_name];
            
            return cell;
        }
        else if(indexPath.section == 1)
        {
            static NSString* cellIdentify = @"cellIdentify2";
            BMSQ_PersonInfoCell* cell = (BMSQ_PersonInfoCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
            if (cell == nil)
            {
                cell = [[BMSQ_PersonInfoCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                //cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            }
            
            [cell setTextEnable:btnRightBar.selected];
            
            if (indexPath.row == 0)
            {
                [cell setCellValue:@"昵称" title:[gloabFunction changeNullToBlank:strNickName]];
            }
            else if (indexPath.row == 1)
            {
                [cell setCellValue:@"姓名" title:[gloabFunction changeNullToBlank:strName]];
            }
            else if (indexPath.row == 2)
            {
                NSString* strSexTmp = [m_dicUserInfo objectForKey:@"sex"];
                if ([strSex isEqualToString:@"M"])
                {
                    strSexTmp = @"男";
                }
                else
                {
                    strSexTmp = @"女";
                }
                
                [cell setTextEnable:NO];
                [cell setCellValue:@"性别" title:strSexTmp];
            }
            else if (indexPath.row == 3)
            {
                [cell setCellValue:@"地区" title:[gloabFunction changeNullToBlank:strCity]];
                [cell setTextEnable:NO];
            }
            else
            {
                [cell setCellValue:@"个性签名" title:[gloabFunction changeNullToBlank:strSign]];
            }
            
            cell.lb_content.tag = indexPath.row;
            cell.delegate = self;
            return cell;
        }
        else
        {
            static NSString* cellIdentify = @"cellIdentify3";
            BMSQ_PersonInfoCell* cell = (BMSQ_PersonInfoCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
            if (cell == nil)
            {
                cell = [[BMSQ_PersonInfoCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
//                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            }
            
            if (indexPath.row == 0)
            {
                NSString *str = [gloabFunction changeNullToBlank:[m_dicUserInfo objectForKey:@"mobileNbr"]];
                if (![str  isEqual: @""]) {
                    NSMutableString *str1 = [[NSMutableString alloc] initWithString:[NSString stringWithFormat:@"%@",str]];
                    [str1 replaceCharactersInRange:NSMakeRange(3, 4) withString:@"****"];
                    NSLog(@"%@",str1);
                }
               
                
                
                [cell setCellValue:@"手机号" title:str];
            }
            
            
            return cell;
        }

    }
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (tableView == m_tableViewCity)
    {
        return 1;
    }
    return 3;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    m_tableViewCity.hidden = YES;
    
    if (tableView == m_tableViewCity)
    {
        strCity = [[NSString alloc]initWithFormat:@"%@",[[m_dataSourceCity objectAtIndex:indexPath.row] objectForKey:@"name"]];
        
        [m_tableView reloadData];
        return;
    }
    if (indexPath.section == 0)
    {
        if (!btnRightBar.selected)
        {
            return;
        }
        if (indexPath.row == 0 || indexPath.row == 1)
        {
            UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:nil otherButtonTitles:@"相机选择",@"拍照", nil];
            sheet.tag = 101;
            [sheet showInView:self.view];
            
        }
        
    }
    else if (indexPath.section == 1)
    {
        if (!btnRightBar.selected)
        {
            return;
        }
        
        if (indexPath.row == 2)
        {
            //修改性别
            UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:nil otherButtonTitles:@"男",@"女", nil];
            sheet.tag = 100;
            [sheet showInView:self.view];
        }
        else if(indexPath.row == 3)
        {
            //修改城市
            
            m_tableViewCity.hidden = NO;
            
        }
        else if(indexPath.row == 4)
        {
//            BMSQ_ModifySignController* signCtrl = [[BMSQ_ModifySignController alloc]init];
//            signCtrl.modifyInfo = [gloabFunction changeNullToBlank:[m_dicUserInfo objectForKey:@"signature"]];
//            signCtrl.mobileNbr = [gloabFunction changeNullToBlank:[m_dicUserInfo objectForKey:@"mobileNbr"]];
//            [self.navigationController pushViewController:signCtrl animated:YES];
        }
    }
    else if (indexPath.section == 2)
    {
        if (indexPath.row == 1)
        {
            BMSQ_modifyPwdViewController* modifyCtrl = [[BMSQ_modifyPwdViewController alloc]init];
            modifyCtrl.userMobile = [m_dicUserInfo objectForKey:@"mobileNbr"];
            [self.navigationController pushViewController:modifyCtrl animated:YES];
        }
    }
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0 && actionSheet.tag == 100)
    {
        //男
        strSex = [[NSString alloc]initWithFormat:@"%@",@"M"];
    }
    else if(buttonIndex == 1 && actionSheet.tag == 100)
    {
        //女
         strSex = [[NSString alloc]initWithFormat:@"%@",@"F"];
    }
    else if(actionSheet.tag == 101)
    {
        if (buttonIndex == 1) {
            // 拍照
            if ([self isCameraAvailable] && [self doesCameraSupportTakingPhotos]) {
                UIImagePickerController *controller = [[UIImagePickerController alloc] init];
                controller.sourceType = UIImagePickerControllerSourceTypeCamera;
                if ([self isFrontCameraAvailable]) {
                    controller.cameraDevice = UIImagePickerControllerCameraDeviceFront;
                }
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
            
        } else if (buttonIndex == 0) {
            // 从相册中选取
            if ([self isPhotoLibraryAvailable]) {
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
        }
    }
    [m_tableView reloadData];
}

- (void)updateUserInfo:(NSString*)value modifyKey:(NSString*)key
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[m_dicUserInfo objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];

    [params setObject:value forKey:key];
   
    NSString* vcode = [gloabFunction getSign:@"updateUserInfo" strParams:[m_dicUserInfo objectForKey:@"mobileNbr"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    //[ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updateUserInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
//        if ([[responseObject objectForKey:@"code"] integerValue] == 50000)
//        {
//            [self showAlertView:@"修改成功"];
//        }
//        else
//        {
//            [self showAlertView:@"修改失败"];
//        }
        
        [m_dicUserInfo setValue:value forKey:key];
        [m_tableView reloadData];
        [ProgressManage closeProgress];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

#pragma mark publishCellDelegate

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    currentTextField = textField;
    NSIndexPath *indexPath = [m_tableView indexPathForCell:(UITableViewCell *) [m_tableView viewWithTag:currentTextField.tag]];
    
    //这里要看textField 是直接加到cell 上的还是加的 cell.contentView上的
    
    BMSQ_PersonInfoCell *cell = (BMSQ_PersonInfoCell *) [[textField superview] superview];
    indexPath = [m_tableView indexPathForCell:cell];
    
    [m_tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewScrollPositionTop animated:YES];
}

- (void)textFieldDoneEditing:(UITextField *)textField
{
    currentTextField = (UITextField *)textField;
    [currentTextField resignFirstResponder];
    [textField resignFirstResponder];
    [m_tableView scrollRectToVisible:currentTextField.frame animated:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    currentTextField = (UITextField *)textField;
    [currentTextField resignFirstResponder];
    [textField resignFirstResponder];
    [m_tableView scrollRectToVisible:currentTextField.frame animated:YES];
    [textField resignFirstResponder];

    return YES;
}

- (void)textFieldWithText:(UITextField *)textField
{
    switch (textField.tag) {
        case 0:
            strNickName = [[NSString alloc]initWithString:textField.text];
            break;
        case 1:
            strName = [[NSString alloc]initWithString:textField.text];
            break;
        case 3:
            strCity = [[NSString alloc]initWithString:textField.text];
            break;
        case 4:
            strSign = [[NSString alloc]initWithString:textField.text];
            break;
        default:
            break;
    }
    
}


//获取已开通的城市
- (void)listZhejiangCity
{
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"listZhejiangCity" withParameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        m_dataSourceCity = [[NSMutableArray alloc]initWithArray:responseObject];
        [m_tableViewCity reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
}



#pragma mark VPImageCropperDelegate
- (void)imageCropper:(VPImageCropperViewController *)cropperViewController didFinished:(UIImage *)editedImage {
    iv_logo.image = editedImage;
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
        // TO DO
        DAProgressOverlayView *progressOverlayView = [[DAProgressOverlayView alloc] initWithFrame:iv_logo.frame];
        progressOverlayView.overlayColor = [UIColor colorWithRed:0.3 green:0.3 blue:0.3 alpha:0.5];
        progressOverlayView.outerRadiusRatio = 0.7;
        progressOverlayView.innerRadiusRatio = 0.5;
        progressOverlayView.layer.cornerRadius = 35;
        progressOverlayView.layer.masksToBounds = YES;
        [iv_logo.superview addSubview:progressOverlayView];
        [iv_logo.superview bringSubviewToFront:progressOverlayView];
        [progressOverlayView displayOperationWillTriggerAnimation];
        [self httpImageRequest:editedImage withObj:progressOverlayView withIndex:0];
        
        
    }];
}

- (void)imageCropperDidCancel:(VPImageCropperViewController *)cropperViewController {
    [cropperViewController dismissViewControllerAnimated:YES completion:^{
    }];
}

/**
 *  @author lxm, 15-08-02 16:08:03
 *
 *  图片上传接口
 */
- (void)httpImageRequest:(UIImage *)image withObj:(NSObject *)object withIndex:(int)index
{
    NSData *dataObj = UIImageJPEGRepresentation(image, 0.00000001);
    
    /**
     *  @author lxm, 15-08-02 18:08:58
     *
     */
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
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
            NSLog(@"Error: %@",aString );
            
        }
        else
        {
            NSLog(@"Success: %@ %@", response, responseObject);
            [(DAProgressOverlayView *)object removeFromSuperview];
             strAvatarUrl = [[NSString alloc]initWithFormat:@"%@",[responseObject objectForKey:@"code"]];

        }
        
    }];
    
    [uploadTask resume];
    [progressData addObserver:object
                   forKeyPath:@"fractionCompleted"
                      options:NSKeyValueObservingOptionNew
                      context:NULL];
    
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

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, 100.0f, self.view.frame.size.width, self.view.frame.size.width) limitScaleRatio:3.0];
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



#pragma mark camera utility
- (BOOL) isCameraAvailable{
    return [UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera];
}

- (BOOL) isRearCameraAvailable{
    return [UIImagePickerController isCameraDeviceAvailable:UIImagePickerControllerCameraDeviceRear];
}

- (BOOL) isFrontCameraAvailable {
    return [UIImagePickerController isCameraDeviceAvailable:UIImagePickerControllerCameraDeviceFront];
}

- (BOOL) doesCameraSupportTakingPhotos {
    return [self cameraSupportsMedia:(__bridge NSString *)kUTTypeImage sourceType:UIImagePickerControllerSourceTypeCamera];
}

- (BOOL) isPhotoLibraryAvailable{
    return [UIImagePickerController isSourceTypeAvailable:
            UIImagePickerControllerSourceTypePhotoLibrary];
}
- (BOOL) canUserPickVideosFromPhotoLibrary{
    return [self
            cameraSupportsMedia:(__bridge NSString *)kUTTypeMovie sourceType:UIImagePickerControllerSourceTypePhotoLibrary];
}
- (BOOL) canUserPickPhotosFromPhotoLibrary{
    return [self
            cameraSupportsMedia:(__bridge NSString *)kUTTypeImage sourceType:UIImagePickerControllerSourceTypePhotoLibrary];
}

- (BOOL) cameraSupportsMedia:(NSString *)paramMediaType sourceType:(UIImagePickerControllerSourceType)paramSourceType{
    __block BOOL result = NO;
    if ([paramMediaType length] == 0) {
        return NO;
    }
    NSArray *availableMediaTypes = [UIImagePickerController availableMediaTypesForSourceType:paramSourceType];
    [availableMediaTypes enumerateObjectsUsingBlock: ^(id obj, NSUInteger idx, BOOL *stop) {
        NSString *mediaType = (NSString *)obj;
        if ([mediaType isEqualToString:paramMediaType]){
            result = YES;
            *stop= YES;
        }
    }];
    return result;
}


@end
