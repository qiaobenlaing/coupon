//
//  EnrolStudentsViewController.m
//  BMSQS
//
//  Created by gh on 16/3/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "EnrolStudentsViewController.h"
#import "UIImageView+WebCache.h"
#import "SVProgressHUD.h"
#import "AFURLSessionManager.h"
#import "SVProgressHUD.h"
#import "OpenClassUtil.h"

@interface EnrolStudentsViewController () <UITableViewDataSource, UITableViewDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate>

@property (nonatomic, strong)UITableView *tableView;

@property (nonatomic, strong)NSString *clickImageID;

@property (nonatomic, strong)NSString *advUrl; //广告图
@property (nonatomic, strong)NSString *recruitUrl; //招生图
@property (nonatomic, strong)NSString *recruitCode;//招生启示编码

@property (nonatomic, strong)UIAlertController *alertController;

@end

@implementation EnrolStudentsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    
    [self setNavBackItem];
    [self setNavTitle:@"招生启示"];
    [self setRightBtn];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.tableView];
    
    [self setMyAlertView];
    [self getShopRecruitInfo];
    
}

#pragma mark - UITableView delegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0 && indexPath.row == 1) {
        return APP_VIEW_WIDTH/3;
        
    }else if  (indexPath.section == 1 && indexPath.row == 1) {
        return APP_VIEW_WIDTH*2;
        
    }else {
        return 44;
    }
    
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 2;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            NSString *identifier = @"section0row0";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.text = @"广告位图";
            
            return cell;
        }else if (indexPath.row == 1) {
            NSString *identifier = @"section0row1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH/3)];
                imageView.tag = 2000;
                [cell.contentView addSubview:imageView];
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIImageView *imageView = [cell.contentView viewWithTag:2000];
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, self.advUrl]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                [cell setNeedsLayout];
            }];
            return cell;
            
        }
    }
    
    if (indexPath.section == 1) {
        if (indexPath.row == 0) {
            NSString *identifier = @"section1row0";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.textLabel.text = @"招生全图";
            
            return cell;
        }else if (indexPath.row == 1) {
            NSString *identifier = @"section1row1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH*2)];
                imageView.tag = 2000;
                [cell.contentView addSubview:imageView];
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIImageView *imageView = [cell.contentView viewWithTag:2000];

            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, self.recruitUrl]] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
                [cell setNeedsLayout];
            }];
            return cell;
            
        }
    }
    
    return nil;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0 && indexPath.row == 1) {
        self.clickImageID = @"0";
        [self presentViewController:self.alertController animated:NO completion:^{

        }];
        
        
    }else if  (indexPath.section == 1 && indexPath.row == 1) {
        self.clickImageID = @"1";
        [self presentViewController:self.alertController animated:NO completion:^{
            
        }];
        
    }
    
    
    
    
}

//完成按钮
- (void)btnAction:(UIButton *)btn {
    
    [self editShopRecruit];
    
}



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


- (void)getShopRecruitInfo {
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getShopRecruitInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopRecruitInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        if ([responseObject count]==0) {
            self.advUrl = @"";
            self.recruitUrl = @"";
            self.recruitCode = @"";
        }else {
            self.advUrl = [responseObject objectForKey:@"advUrl"];
            self.recruitUrl = [responseObject objectForKey:@"recruitUrl"];
            self.recruitCode = [responseObject objectForKey:@"recruitCode"];
        }
        
        
        [self.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(@"删除失败");
        
    }];

    
    
}


- (void)editShopRecruit {
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary *updatedic = [[NSMutableDictionary alloc] init];
    [updatedic setObject:self.advUrl forKey:@"advUrl"];
    [updatedic setObject:self.recruitUrl forKey:@"recruitUrl"];
    [updatedic setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [updatedic setObject:self.recruitCode forKey:@"recruitCode"];
    NSString *updateDataStr =[OpenClassUtil jsonStringWithDictionary:updatedic];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.recruitCode forKey:@"recruitCode"];
    [params setObject:updateDataStr forKey:@"updateData"];
    NSString* vcode = [gloabFunction getSign:@"editShopRecruit" strParams:self.recruitCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"editShopRecruit" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"%@", responseObject);
        NSString *code = [responseObject objectForKey:@"code"];
        if (code.intValue == 50000) {
            self.recruitCode = [responseObject objectForKey:@"recruitCode"];
            CSAlert(@"修改完成");
        } else {
            CSAlert([self errorManager:code]);
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(@"修改失败");
        
    }];

}

- (NSString *)errorManager:(NSString *)code{
    NSString *managestr;
    switch (code.intValue) {
        case 20000:
            managestr = @"失败";
            break;
        case 50317:
            managestr = @"商家编码不能为空";
            break;
        case 77022:
            managestr = @"广告图为空";
            break;
        case 77023:
            managestr = @"招生图为空";
            break;
        default:
            managestr = [NSString stringWithFormat:@"错误信息:%@", code];
            break;
    }
    
    return managestr;
    
}


#pragma mark - alertController创建
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
    
    
    NSData *dataObj = UIImageJPEGRepresentation(image, 0.00000001);
    
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
                if (self.clickImageID.intValue == 0) {
                    self.advUrl = code;
                }else if (self.clickImageID.intValue == 1) {
                    self.recruitUrl = code;
                }
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


@end
