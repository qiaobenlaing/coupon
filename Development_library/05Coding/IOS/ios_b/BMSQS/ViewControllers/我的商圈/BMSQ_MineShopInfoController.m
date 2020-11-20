//
//  BMSQ_MyBusinessController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "SLCGRectHelper.h"
#import "UIImageView+WebCache.h"
#import "BMSQ_ShopInfoEditViewController.h"
#import "DecorationShopViewController.h"
#import "BMSQ_ShopHourViewController.h"
#import "BMSQ_StoreImageViewController.h"
#import "BMSQ_StoreQrcodeViewController.h"
#import "BMSQ_PhotoViewController.h"
#import "BMSQ_ShortDesViewController.h"
#import "DAProgressOverlayView.h"
#import "AFURLSessionManager.h"

@interface HeaderCell0 : UITableViewCell


@end

@implementation HeaderCell0

- (void)layoutSubviews{
    [super layoutSubviews];
    
    self.imageView.frame = SLRectMake(APP_VIEW_WIDTH-80, 8, 76, 76);
    self.imageView.backgroundColor = [UIColor whiteColor];
    

    CGRect rect = self.textLabel.frame;
    rect.origin = CGPointMake(15, 25);
    rect.size   = CGSizeMake(self.frame.size.width - rect.origin.x - 44, 22);
    self.textLabel.font = [UIFont systemFontOfSize:15.f];
    self.textLabel.frame = rect;
    
    rect = self.detailTextLabel.frame;
    rect.origin = CGPointMake(CGRectGetMaxX(self.textLabel.frame) +10, CGRectGetMaxY(self.textLabel.frame) +10);
    rect.size.height = self.frame.size.height - rect.origin.y-30;
    rect.size.width  = CGRectGetWidth(self.textLabel.frame);
    self.detailTextLabel.frame = rect;
    self.detailTextLabel.numberOfLines = 0;
    self.detailTextLabel.font = [UIFont systemFontOfSize:14.0];
    self.detailTextLabel.textColor = [UIColor lightGrayColor];
}


@end

#import "BMSQ_MineShopInfoController.h"

@interface BMSQ_MineShopInfoController ()<UITableViewDataSource, UITableViewDelegate,BMSQ_ShopInfoEditViewControllerDelegate, UIActionSheetDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate, VPImageCropperDelegate> {
    
    UIImageView* iv_logo;
    UIView *logoBacview;
}
@property (nonatomic, strong)UISwitch *sw_supportNDR;

@property (nonatomic ,strong) UITableView* tableView;

@property (nonatomic, strong) NSMutableArray * businessHoursAry;

@property (nonatomic, assign)CGFloat shopDeatilH;

@property (nonatomic, strong)NSString *supportNDR;

@end

@implementation BMSQ_MineShopInfoController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"商铺信息"];
    self.businessHoursAry = [@[] mutableCopy];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT+64)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    UIView *footerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 44)];
    _tableView.tableFooterView = footerView;
    [self.view addSubview:_tableView];
    
    
    self.sw_supportNDR = [[UISwitch alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-65, 10, 30, 20)];
    [self.sw_supportNDR addTarget:self action:@selector(switchAct:)  forControlEvents:UIControlEventValueChanged];
//    [View1 addSubview:self.sw_supportNDR];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(sGetShopBasicInfo) name:@"shopHourFinish" object:nil];
    
    [self sGetShopBasicInfo];
    
}

- (void)dealloc {
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"shopHourFinish" object:nil];
    
}

- (void)switchAct:(UISwitch *)sender {
    UISwitch *switchButton = (UISwitch *)sender;
    
    
    NSString *istake;
    if (switchButton.on) { //1 支持隔日 0 不支持隔日
        istake = @"1";
    }else {
        istake = @"0";
    }
    [self updateShopSupportNDR:istake];
}

- (void)updateShopSupportNDR:(NSString *)isSupportNDR {
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    NSString *updateKey = [NSString stringWithFormat:@"supportNDR"];
    NSString *updateValue = [NSString stringWithFormat:@"%@",isSupportNDR];
    
    [params setObject:updateKey forKey:@"updateKey"];
    [params setObject:updateValue forKey:@"updateValue"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];
        
        if (code.intValue == 50000) {
            self.sw_supportNDR.on = isSupportNDR.intValue;
            CSAlert(@"修改成功");
            
        }else {
            self.sw_supportNDR.on = !isSupportNDR.intValue;
            CSAlert(@"修改失败");
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];

}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    int number = [(NSNumber *)self.shopInfoDic[@"isOpenEat"] intValue];
    if (number == 0) {
        return 4;
    }else{
        return 5;
    }

}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
 
    if (section == 0) {
        return 1;
    }
    else if (section == 1) {
        return 4;
    }
    else if (section == 2) {
        return 2;
    }
    else if (section == 3) {
        return 1;
    }else if (section == 4){
        return 1;
    }
    return 0;
    
  
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    if (indexPath.section == 0) {
        static NSString *identitfier = @"HeaderCell";
        HeaderCell0 *cell = (HeaderCell0 *)[tableView dequeueReusableCellWithIdentifier:identitfier ];
        if (!cell) {
            cell = [[HeaderCell0 alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identitfier];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.accessoryType = UITableViewCellAccessoryNone;
        cell.imageView.backgroundColor = [UIColor whiteColor];
        cell.textLabel.text = @"商家头像";
        
        NSString *urlStr = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,_shopInfoDic[@"logoUrl"]];
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        button.frame = cell.imageView.frame;
        button.backgroundColor = [UIColor clearColor];
        [cell addSubview:button];
        
        [button addTarget:self action:@selector(imageViewBtnAct) forControlEvents:UIControlEventTouchUpInside];
        [cell.imageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"] completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
            [cell setNeedsLayout];
            
        }];
        
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        return cell;
    
    }else if (indexPath.section == 2){
        if (indexPath.row == 0) {
            static NSString *identitfier = @"BusinessTimeViewCell";
            BusinessTimeViewCell *cell = (BusinessTimeViewCell *)[tableView dequeueReusableCellWithIdentifier:identitfier];
            if (!cell) {
                cell = [[BusinessTimeViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identitfier];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            cell.titleLB.text = @"营业时间";
            
            if ((NSNull *)_businessHoursAry == [NSNull null]) {
                
            }
            else {
                cell.leftTimeLB.text = @"";
                cell.centerTimeLB.text = @"";
                cell.rightTimeLB.text = @"";
                if (self.businessHoursAry.count == 1) {
                    cell.leftTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[0] objectForKey:@"open"], [_businessHoursAry[0] objectForKey:@"close"]];
                    
                }else if (self.businessHoursAry.count == 2){
                    cell.leftTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[0] objectForKey:@"open"], [_businessHoursAry[0] objectForKey:@"close"]];
                    cell.centerTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[1] objectForKey:@"open"], [_businessHoursAry[1] objectForKey:@"close"]];
                    
                }else if (self.businessHoursAry.count == 3){
                    cell.leftTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[0] objectForKey:@"open"], [_businessHoursAry[0] objectForKey:@"close"]];
                    cell.centerTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[1] objectForKey:@"open"], [_businessHoursAry[1] objectForKey:@"close"]];
                    cell.rightTimeLB.text = [NSString stringWithFormat:@"%@-%@",[_businessHoursAry[2] objectForKey:@"open"], [_businessHoursAry[2] objectForKey:@"close"]];
                }
            }
            
            return cell;
            
        }else if (indexPath.row == 1) {
            
            static NSString *identitfier = @"supportNDRCell";
             UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identitfier];
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identitfier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                cell.textLabel.font = [UIFont systemFontOfSize:14.f];
                
//                UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 1)];
//                lineView.backgroundColor = APP_CELL_LINE_COLOR;
//                [cell.contentView addSubview:lineView];
            }
            cell.textLabel.text = @"隔日退货";
            [cell.contentView addSubview:self.sw_supportNDR];
            return cell;

        }
        

    }
    
    else {
        
        
        int number = [(NSNumber *)self.shopInfoDic[@"isOpenEat"] intValue];
        static NSString *identitfierInput = @"inputCell";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identitfierInput];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:identitfierInput];
            
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 120, 50)];
            [button setImage:[UIImage imageNamed:@"closeStatus"] forState:UIControlStateNormal];
            [button setImage:[UIImage imageNamed:@"openStatus"] forState:UIControlStateSelected];
            [cell addSubview:button];
            [button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
            button.hidden = YES;
            button.tag = 100+indexPath.row;
            
            
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.textLabel.font = [UIFont systemFontOfSize:14.f];
        cell.detailTextLabel.font = [UIFont systemFontOfSize:14.f];
        UIButton *button = (UIButton *)[cell viewWithTag:100+indexPath.row];
        button.hidden = YES;
        if (indexPath.section == 1) {
            
            if (indexPath.row == 0) {
                
                cell.textLabel.text = @"店铺名称";
                cell.detailTextLabel.text = _shopInfoDic[@"shopName"];
                
            }
            if (indexPath.row == 1) {
                cell.textLabel.text = @"店长号码";
                cell.detailTextLabel.text = _shopInfoDic[@"mobileNbr"];
                
            }
            if (indexPath.row == 2) {
                cell.textLabel.text = @"联系电话";
                cell.detailTextLabel.text = _shopInfoDic[@"tel"];
            }
            if (indexPath.row == 3) {
                cell.textLabel.text = @"店铺地址";
                cell.detailTextLabel.text = [NSString stringWithFormat:@"%@%@%@",_shopInfoDic[@"province"],_shopInfoDic[@"city"],_shopInfoDic[@"street"]];
                cell.detailTextLabel.numberOfLines = 2;
                cell.detailTextLabel.textAlignment = NSTextAlignmentLeft;
            }
            
        }
        if (number == 0){
            if (indexPath.section == 3) {
                
                static NSString *idenftier = @"shopInfoCell4";
                cell = [tableView dequeueReusableCellWithIdentifier:idenftier];
                if (cell == nil) {
                    cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenftier];
                    UILabel *labelTitle = [[UILabel alloc]initWithFrame:CGRectMake(12, 0, 100, 30)];
                    labelTitle.text = @"店铺简介";
                    //               labelTitle.textColor = APP_TEXTCOLOR;
                    labelTitle.font = [UIFont systemFontOfSize:14];
                    [cell addSubview:labelTitle];
                    
                    
                    UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 30, APP_VIEW_WIDTH-20, 0)];
                    //               desLabel.backgroundColor = [UIColor redColor];
                    desLabel.textColor = APP_TEXTCOLOR;
                    desLabel.font = [UIFont systemFontOfSize:13];
                    desLabel.numberOfLines = 0;
                    desLabel.tag = 100;
                    [cell addSubview:desLabel];
                }
                
                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                
                
                UILabel *deslabel = (UILabel *)[cell viewWithTag:100];
                deslabel.text = _shopInfoDic[@"shortDes"];
                CGSize size = [deslabel.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: deslabel.font} context:nil].size;
                deslabel.frame = CGRectMake(10, 30, size.width, size.height);
                
            }
            
        }else{
            
            if (indexPath.section == 3) {
                
                cell.textLabel.text = @"餐厅设置";
                
                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                
            } else if (indexPath.section == 4) {
                static NSString *idenftier = @"shopInfoCell4";
                cell = [tableView dequeueReusableCellWithIdentifier:idenftier];
                
                if (cell == nil) {
                    cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenftier];
                    UILabel *labelTitle = [[UILabel alloc]initWithFrame:CGRectMake(12, 0, 100, 30)];
                    labelTitle.text = @"店铺简介";
                    labelTitle.font = [UIFont systemFontOfSize:14];
                    [cell addSubview:labelTitle];
                    
                    
                    UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 30, APP_VIEW_WIDTH-20, 0)];
                    desLabel.textColor = APP_TEXTCOLOR;
                    desLabel.font = [UIFont systemFontOfSize:13];
                    desLabel.numberOfLines = 0;
                    desLabel.tag = 100;
                    [cell addSubview:desLabel];
                }
                
                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                UILabel *deslabel = (UILabel *)[cell viewWithTag:100];
                deslabel.text = _shopInfoDic[@"shortDes"];
                deslabel.frame = CGRectMake(10, 30, APP_VIEW_WIDTH-20, self.shopDeatilH);
                
            }
            
        }
        
        return cell;
    }
    
    return nil;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    int number = [(NSNumber *)self.shopInfoDic[@"isOpenEat"] intValue];
    if (number == 1) {
        
        if (indexPath.section == 0) {
            return 80;
        } else if (indexPath.section == 4){
            return  40+self.shopDeatilH;
        } else if (indexPath.section == 2 && indexPath.row == 0){
            
            return 60;
        }
        
        return 44;
        
    }else{
        
        if (indexPath.section == 0) {
            return 80;
            
        } else if (indexPath.section == 3){
            return  40+self.shopDeatilH;
            
        } else if (indexPath.section == 2){
            return 60;
        }
    
        return 44;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (section == 0) {
        return 0;
    }
    return 13;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    int number = [(NSNumber *)self.shopInfoDic[@"isOpenEat"] intValue];
    
//    if(indexPath.section==0&&indexPath.row==0){//修改头像
//        UIActionSheet *choiceSheet = [[UIActionSheet alloc] initWithTitle:nil
//                                                                 delegate:self
//                                                        cancelButtonTitle:@"取消"
//                                                   destructiveButtonTitle:nil
//                                                        otherButtonTitles:@"从相册中选取", nil];
//        choiceSheet.delegate = self;
//        choiceSheet.tag = 800;
//        [choiceSheet showInView:self.view];
//        
//    }
    
    NSString *title;
    NSString *key;
    
    if (indexPath.section == 2 || [self.businessHoursAry isEqual:@"<null>"]) {
        title = @"营业时间";
        key   = @"time";
        
        BMSQ_ShopHourViewController *editVC = [[BMSQ_ShopHourViewController alloc] init];
        editVC.delegate = self;
        
        if ((NSNull *)_businessHoursAry == [NSNull null]) {
            editVC.shopTimeAry = [[NSMutableArray alloc] init];
        }else {
            editVC.shopTimeAry = [NSMutableArray arrayWithArray:self.businessHoursAry];
        }
        
        editVC.shopDataDic = @{@"key":key,@"title":title};
        [self.navigationController pushViewController:editVC animated:YES];
        return;
        
    }
    if (number == 1) {
        
        if (indexPath.section == 3) {
            BMSQ_DineSetUpViewController * dineVC = [[BMSQ_DineSetUpViewController alloc] init];
            dineVC.isOpenEat = _shopInfoDic[@"isOpenEat"];
            dineVC.isOpenTakeout = _shopInfoDic[@"isOpenTakeout"];
            dineVC.tableNbrSwitch = _shopInfoDic[@"tableNbrSwitch"];
            
            [self.navigationController pushViewController:dineVC animated:YES];
            return;
        }
        
        if (indexPath.section == 4) {
            title = @"店铺介绍";
            key   = @"shortDes";
            
            BMSQ_ShortDesViewController *desVC = [[BMSQ_ShortDesViewController alloc] init];
            desVC.delegate = self;
            desVC.textValue = _shopInfoDic[@"shortDes"];
            desVC.shopDataDic = @{@"key":key,@"title":title};
            [self.navigationController pushViewController:desVC animated:YES];
            return;
        }
        
    }else{
        
        if (indexPath.section == 3) {
            title = @"店铺介绍";
            key   = @"shortDes";
            
            BMSQ_ShortDesViewController *desVC = [[BMSQ_ShortDesViewController alloc] init];
            desVC.delegate = self;
            desVC.textValue = _shopInfoDic[@"shortDes"];
            desVC.shopDataDic = @{@"key":key,@"title":title};
            [self.navigationController pushViewController:desVC animated:YES];
            return;
        }
    }
}

// 调用手机相册，从里面选照片
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    if (actionSheet.tag == 800) {
        if (buttonIndex == 0){
            NSLog(@"相册选取图片作为头像");
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

- (void)updateShopLogo:(NSString *)imageUrl {
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    NSString *updateKey = [NSString stringWithFormat:@"logoUrl"];
    NSString *updateValue = [NSString stringWithFormat:@"%@",imageUrl];
    
    [params setObject:updateKey forKey:@"updateKey"];
    [params setObject:updateValue forKey:@"updateValue"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];
        
        if (code.intValue == 50000) {
            [self sGetShopBasicInfo];
            CSAlert(@"修改成功");
            
        }else {
            CSAlert(@"修改失败");
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];
    
    
    
    
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^() {
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        //            CGSize aFactSize = [[UIScreen mainScreen] bounds].size;
        portraitImg = [self imageByScalingToMaxSize:portraitImg];
        VPImageCropperViewController *imgCropperVC = [[VPImageCropperViewController alloc] initWithImage:portraitImg cropFrame:CGRectMake(0, 100.0f, self.view.frame.size.width, self.view.frame.size.width*9/16) limitScaleRatio:3.0];
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

- (void)httpImageRequest:(UIImage *)image withObj:(NSObject *)object withIndex:(int)index {
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
//            NSLog(@"Error: %@",aString );
            [SVProgressHUD dismiss];
            
        }else {
            NSLog(@"%@",responseObject);
            NSString *code = [responseObject objectForKey:@"code"];
            if (code.length>5){
//                code;
//                NSLog(@"%@",responseObject);
                [self updateShopLogo:code];
                
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


#pragma mark- BMSQ_ShopInfoEditViewControllerDelegate
- (void)controller:(BMSQ_ShopInfoEditViewController *)controller DidSuccessEditInfo:(NSDictionary *)info{
    NSMutableDictionary *tempDic =  [_shopInfoDic mutableCopy];
    
    tempDic[info[@"key"]] = info[@"value"];
    self.shopInfoDic = tempDic;
    [self.tableView reloadData];
    
    [self httpRequest:info];

}

#pragma  mark - request
- (void)sGetShopBasicInfo{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetShopBasicInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"sGetShopBasicInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        _shopInfoDic = responseObject;
        
        _businessHoursAry = responseObject[@"businessHours"];
        
        NSString *str = [_shopInfoDic objectForKey:@"shortDes"];
        NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:13.f]};
        CGSize contentSize=[str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-60, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
        
        
        self.sw_supportNDR.on = [[responseObject objectForKey:@"supportNDR"] intValue];
        self.shopDeatilH = contentSize.height<45?45:contentSize.height;
        
        
        self.shopAllMsg = responseObject;
        [weakSelf.tableView reloadData];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}


#pragma  mark - request
- (void)httpRequest:(NSDictionary *)dicTemp
{
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    NSDictionary *dica = @{@"shopCode":[gloabFunction getShopCode],
                           @"tokenCode":[gloabFunction getUserToken],
                           @"vcode":vcode,
                           @"reqtime":[gloabFunction getTimestamp]};

    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:dica];
    [dic setObject:dicTemp[@"value"] forKey:dicTemp[@"key"]];
    [dic setObject:[self.shopInfoDic objectForKey:@"logoUrl"] forKey:@"logoUrl"];
    [dic setObject:[self.shopInfoDic objectForKey:@"type"] forKey:@"type"];
    //[dic setObject:@"imgUrl" forKey:[self.shopInfoDic objectForKey:@"type"]];
    
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){

        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}



-(void)clickButton:(UIButton *)button{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
   
    NSString *updateKey;
    NSString *updateValue ;
    if (button.tag == 100) {
        BOOL btnSelect = !button.selected;
        NSString *btnstr;
        if (btnSelect) {
            btnstr = @"1";
        }else {
            btnstr = @"0";
        }
        updateKey = [NSString stringWithFormat:@"isOpenEat"];
        updateValue = [NSString stringWithFormat:@"%@",btnstr];
        
    }else if (button.tag == 101) {
        updateKey = [NSString stringWithFormat:@"isOpenTakeout"];
        BOOL btnSelect = !button.selected;
        NSString *btnstr;
        if (btnSelect) {
            btnstr = @"1";
        }else {
            btnstr = @"0";
        }
        updateValue = [NSString stringWithFormat:@"%@",btnstr];
        
    }
  
    
    [params setObject:updateKey forKey:@"updateKey"];
    [params setObject:updateValue forKey:@"updateValue"];
    
    __block  typeof(self)weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {

        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];

        if (code.intValue == 50000) {
            button.selected = !button.selected;
            
        }else {
            CSAlert(@"修改失败");
            
        }

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];
    
    
}

- (void)imageViewBtnAct {
    
    NSLog(@"点击图片");
    
//    self.frame = [[UIScreen mainScreen]bounds];
//    self.backgroundColor = [UIColor blackColor];
//    self.pagingEnabled = YES;
//    
//    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesture)];
//    [self addGestureRecognizer:tapGesture];
    logoBacview = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    logoBacview.backgroundColor = [UIColor blackColor];
    [self.view addSubview:logoBacview];
    UIImageView *logoImageView = [[UIImageView alloc] initWithFrame:logoBacview.bounds];
    logoImageView.contentMode = UIViewContentModeScaleAspectFit;
    [logoImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,_shopInfoDic[@"logoUrl"]]]];
    [logoBacview addSubview:logoImageView];
    
    
    UIApplication *app = [UIApplication sharedApplication];
    UIWindow *window = app.keyWindow;

    [window addSubview:logoBacview];
    
    
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesture)];
    [logoBacview addGestureRecognizer:tapGesture];
}

- (void)tapGesture{
    [logoBacview removeFromSuperview];
    
}


@end
