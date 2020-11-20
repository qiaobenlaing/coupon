//
//  BMSQ_HomeController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeController.h"
#import "BMSQ_HomeActionCell.h"
#import "MD5.h"
#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"

#import "BMSQ_SettlerViewController.h"
#import "BMSQ_CountViewController.h"
#import "BMSQ_mineOrderViewController.h"
#import "BMSQ_ShopOrderViewController.h"
#import "APService.h"
#import "UIImageView+WebCache.h"

#import "BMSQ_AwayOrderViewController.h"
#import "ActImageTextAttachment.h"

@interface BMSQ_HomeController ()<BMSQ_HomeActionCellDelegate>
{
    UITableView* m_tableView;
    
    NSTimer * timer;
    UIImageView* _line;
    BOOL upOrdown;
    int num;
    
    NSString * staffCodeStr;
    NSString * tokenCodeStr;
    
    NSString * pushStr;
    
    int count;
    
}

@property (nonatomic, assign)BOOL isEduTraining;

@property (nonatomic, strong)NSString *countStr;
@property (nonatomic, strong)UIButton *settlerBtn;
@property (nonatomic, strong)UIView * pushView;//推送视图
@property (nonatomic, strong) NSDictionary * receiveDic;
@property (nonatomic, strong) NSMutableArray * receiveAry;


@end

@implementation BMSQ_HomeController



- (id)initWithCoder:(NSCoder *)aDecoder {
    
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.receiveAry = [[NSMutableArray alloc] init];
    }
    
    return self;
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self getShopOrderType];
    [self getShopAllBrowseQuantity];
    [self sGetShopBasicInfo];
    [self setMyTitle];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.isEduTraining = NO;
    
    // Do any additional setup after loading the view.
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(setMyTitle) name:@"setMyTitle" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(sGetShopBasicInfo) name:@"ShopNameFinish" object:nil];
    
    
    NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    [defaultCenter addObserver:self
                      selector:@selector(networkDidSetup:)
                          name:@"theValueOf"
                        object:nil];
    
    NSNotificationCenter * defaultCenter1 = [NSNotificationCenter defaultCenter];
    [defaultCenter1 addObserver:self
                      selector:@selector(gatheringListNot:)
                          name:@"gatheringList"
                        object:nil];
    
    
    
    UIButton* btnRightBar = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44)];
    btnRightBar.backgroundColor = [UIColor clearColor];
    [btnRightBar addTarget:self action:@selector(btnRightClick) forControlEvents:UIControlEventTouchUpInside];
    [btnRightBar setImage:[UIImage imageNamed:@"iv_scan"] forState:UIControlStateNormal];
    
    [self setNavRightBarItem:btnRightBar];
    
    [self setViewUp];
    
    
   
    

}

-(void)setMyTitle{
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSString *shopName = [uD objectForKey:@"shopName"];
    
//    [self setNavTitle:shopName];
    [self getStaffShopList];
    
}



#pragma mark ---- shopNameButClick
- (void)shopNameButClick:(UIButton *)sender
{
    BMSQ_SelectShopViewController * selectShopVC = [[BMSQ_SelectShopViewController alloc] init];
    selectShopVC.staffCode = staffCodeStr;
    selectShopVC.tokenCode = tokenCodeStr;
    selectShopVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:selectShopVC animated:YES];
}

#pragma mark ----- 推送提醒
- (void)pushPrompt
{
    self.pushView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 30)];
    _pushView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:_pushView];
    
    
    UILabel * pushLB = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH - 20, _pushView.frame.size.height)];
    pushLB.font = [UIFont systemFontOfSize:14];
    pushLB.text = pushStr;
    NSLog(@"%@", pushStr);
    [_pushView addSubview:pushLB];
    
    UIButton * shutDownBut = [UIButton buttonWithType:UIButtonTypeCustom];
    shutDownBut.frame = CGRectMake(APP_VIEW_WIDTH - 30, 0, 30, _pushView.frame.size.height);
    [shutDownBut setImage:[UIImage imageNamed:@"del_msg"] forState:UIControlStateNormal];
    [shutDownBut addTarget:self action:@selector(shutDownButClick:) forControlEvents:UIControlEventTouchUpInside];
    [_pushView addSubview:shutDownBut];
}

// 接到通知的回调方法
- (void)networkDidSetup:(NSNotification *)notification
{
    // _j_msgid  alert 031d2d9891b
    NSDictionary * dic = notification.object;
    NSString * str = dic[@"aps"][@"alert"];
    pushStr = [NSString stringWithFormat:@"温馨提示:%@", str];
    [self pushPrompt];
}

- (void)gatheringListNot:(NSNotification *)notification
{
    NSDictionary * dic = notification.object;
    
    [self.receiveAry insertObject:dic atIndex:0];
    [m_tableView reloadData];
    NSLog(@"%@", dic);
    
}


// 移除通知
- (void)dealloc
{
     NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    [defaultCenter removeObserver:self
                             name:@"theValueOf"
                           object:nil];
    
    // gatheringList
    NSNotificationCenter *gatheringList = [NSNotificationCenter defaultCenter];
    [gatheringList removeObserver:self
                             name:@"gatheringList"
                           object:nil];
    
}

#pragma mark --- setViewUp
- (void)setViewUp
{
    [self setNavTitle:@"首页"];
    
    
//    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH/2, 79)];
//    topView.backgroundColor = [UIColor whiteColor];
//    [self.view addSubview:topView];
//    
//    // 小箭头图标
////    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 35, 10, 15)];
////    imageView.backgroundColor = [UIColor greenColor];
////    [imageView setImage:[UIImage imageNamed:@"iv_Right"]];
////    [topView addSubview:imageView];
//    
//    
//    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 55, APP_VIEW_WIDTH/2, 10)];
//    label.text = @"总浏览量";
//    label.textAlignment = NSTextAlignmentCenter;
//    label.font = [UIFont systemFontOfSize:12];
//    label.textColor = APP_TEXTCOLOR;
//    [topView addSubview:label];
//    //0上面的小人头像
//    UIImageView *iconimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 15, 20, 18)];
//    [iconimageView setImage:[UIImage imageNamed:@"manager.png"]];
//    [topView addSubview:iconimageView];
//    iconimageView.center = CGPointMake(APP_VIEW_WIDTH/4, 15);
//
//    self.countLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH/2, 40)];
//    self.countLabel.textColor = APP_TEXTCOLOR;
//    self.countLabel.font = [UIFont systemFontOfSize:20];
//    self.countLabel.text = @"";
//    self.countLabel.textAlignment = NSTextAlignmentCenter;
//    [topView addSubview:self.countLabel];
    
    //浏览量
//    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(gotoCountVC)];
//    [topView addGestureRecognizer:tapGesture];


    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:m_tableView];
    
    
    self.settlerBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-44-40, APP_VIEW_WIDTH, 38)];
    self.settlerBtn.backgroundColor =APP_NAVCOLOR;
    [self.settlerBtn setTitle:@"去入驻" forState:UIControlStateNormal];
    [self.settlerBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.settlerBtn.titleLabel.font =[UIFont boldSystemFontOfSize:14];
    [self.settlerBtn addTarget:self action:@selector(gotoSetterVC) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.settlerBtn];
    self.settlerBtn.hidden = YES;
    
    [self getLogin];
    
    
    if ([gloabFunction isLogin]) {
        
        [self getGuideInfo];
    }
    
}

//
- (void)getShopOrderType{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopOrderType" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getShopOrderType" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSDictionary *dic = responseObject;
        NSString *isCatering = [NSString stringWithFormat:@"%@",[dic objectForKey:@"isCatering"]];
        NSString *isOuttake = [NSString stringWithFormat:@"%@",[dic objectForKey:@"isOuttake"]];
        NSString *shopTypeFood,*shopTypeNoFood,*shopTypeTakeOut;
        shopTypeFood=[isCatering isEqualToString:@"1"]?@"1":@"0";
        shopTypeTakeOut=[isOuttake isEqualToString:@"1"]?@"1":@"0";
        shopTypeNoFood=[isCatering isEqualToString:@"0"]?@"1":@"0";
        
        NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
        [uD setObject:shopTypeFood forKey:SHOPTYPE_FOOD];
        [uD setObject:shopTypeTakeOut forKey:SHOPTYPE_FOOD_TAKEOUT];
        [uD setObject:shopTypeNoFood forKey:SHOPTYPE_NO_FOOD];
        [uD synchronize];;
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        
    }];
    
}

//欢迎页图片
- (void)getGuideInfo {
    [self initJsonPrcClient:@"0"];
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeClear];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:@"0" forKey:@"appType"]; //0-商家端  1-用户端

    [self.jsonPrcClient invokeMethod:@"getGuideInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"%@",responseObject);
        [SVProgressHUD dismiss];
        UIImageView *imageView  = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
        imageView.tag = 9999;
        [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL, [responseObject objectForKey:@"value"]]] placeholderImage:[UIImage imageNamed:@"LaunchImage"]];
        
        [self.view.window addSubview:imageView];
        [self performSelector:@selector(timeAction) withObject:nil afterDelay:2.0];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        NSLog(@"%@",error);
    }];

}


- (void)timeAction {
    UIImageView *iv = [self.view.window viewWithTag:9999];
    iv.hidden = YES;
    
}

//登录
- (void)getLogin
{
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    NSString *registrationId = [userDefault objectForKey:RegistratcionID];
    
    if([uD objectForKey:@"mobileNbr"]&&[uD objectForKey:@"password"]){
        NSDictionary *dic =@{@"mobileNbr":[uD objectForKey:@"mobileNbr"],
                             @"password":[uD objectForKey:@"password"],
                             @"registrationId":registrationId,
                             @"loginType":@"0"};
        [SVProgressHUD showWithStatus:ProgressHudStr];
        [self initJsonPrcClient:@"0"];
        [self.jsonPrcClient invokeMethod:@"login" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
            [SVProgressHUD dismiss];
            int resCode = [[responseObject objectForKey:@"code"] intValue];
            switch (resCode) {
                case 50000:{
                    
                    staffCodeStr = responseObject[@"staffCode"];
                    tokenCodeStr = responseObject[@"tokenCode"];
                    
                    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                    [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                    [uD setObject:[uD objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];
                    [uD setObject:[uD objectForKey:@"password"] forKey:@"password"];
                    [uD setObject:[responseObject objectForKey:@"staffCode"] forKey:@"staffCode"];
                    [uD synchronize];
                    NSString* vcode = [gloabFunction getSign:@"sGetShopBasicInfo" strParams:[gloabFunction getShopCode]];
                    
                    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
                        return;
                    
                    [self sGetShopBasicInfo];
                    
                }
                    break;
                default:{
                    [self enterLoginVC];
                }
                    break;
            }
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [SVProgressHUD dismiss];
            [self enterLoginVC];
        }];
        return;
    }else{
        [self enterLoginVC];
    }
    
}




- (void)enterLoginVC
{
    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
    BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
    BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
    [self presentViewController:nav animated:YES completion:^{
        
    }];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0) {
        if ([[gloabFunction getisCatering] intValue] == 2) {
            return 291 + APP_VIEW_WIDTH;
            
        }else {
            return 291;
            
        }
        
    } else if (indexPath.section == 1) {
        return 105;
        
    }

    return 0;

}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (section == 0)
    {
//        return 15;
    }
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{

    
    if (indexPath.section == 0) {
        
    
        static NSString* cellIdentify = @"BMSQ_HomeActionCell";
        BMSQ_HomeActionCell* cell = (BMSQ_HomeActionCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[BMSQ_HomeActionCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.homeDelegate = self;
        }
        if ([[gloabFunction getisCatering] intValue] == 2) {
            [cell setBottomView];
        }else {
            [cell removeBottomView];
        }
        
        
        UILabel *countLabel = [cell.contentView viewWithTag:2000];
        countLabel.text = self.countStr;
        
        
        return cell;

    }else if (indexPath.section == 1){
        static NSString* cellIdentify = @"GatheringListViewCell";
        GatheringListViewCell * cell = (GatheringListViewCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (!cell) {
            cell = [[GatheringListViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setCellValue:self.receiveAry[indexPath.row]];
       
        
        
        return cell;
    }
    
    
    
    return nil;
    

}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (self.receiveAry.count > 0){
        
        return 2;
        
    }else{
        
        return 1;
    }
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
        
    }else{
        
        return self.receiveAry.count;
        
    }
    
    
}



#pragma mark - 获得商户总浏览量
-(void)getShopAllBrowseQuantity{
    [self initJsonPrcClient:@"1"];

    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopAllBrowseQuantity" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopAllBrowseQuantity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSString * i = [NSString stringWithFormat:@"%@", responseObject];
        weakSelf.countStr = [NSString stringWithFormat:@"%@",i];
        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
}


#pragma mark - 商家是否入驻
-(void)sGetShopBasicInfo{
    [self initJsonPrcClient:@"1"];
//    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetShopBasicInfo" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:@"0" forKey:@"page"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sGetShopBasicInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        [SVProgressHUD dismiss];
        
        NSDictionary *dic = responseObject;
        NSString *shopStatus = [NSString stringWithFormat:@"%@",[dic objectForKey:@"shopStatus"]];
        NSUserDefaults *userDef =[NSUserDefaults standardUserDefaults];
        [userDef setObject:[dic objectForKey:@"isCatering"] forKey:@"isCatering"];
        [userDef setObject:[dic objectForKey:@"shopName"] forKey:@"shopName"];
        [userDef setObject:[dic objectForKey:@"logoUrl"] forKey:@"logoUrl"];
        [userDef synchronize];
        
        
        if ([shopStatus intValue] == 1) {
            weakSelf.settlerBtn.hidden = NO;
        }else{
            weakSelf.settlerBtn.hidden = YES;

        }
        [[NSNotificationCenter defaultCenter]postNotificationName:@"setMyTitle" object:nil];
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        [SVProgressHUD dismiss];
        
    }];
}

#pragma mark - 拥有多少个商铺
- (void)getStaffShopList
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getStaffCode] forKey:@"staffCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getStaffShopList" strParams:[gloabFunction getStaffCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:[NSNumber numberWithInt:1] forKey:@"page"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStaffShopList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        count = [responseObject[@"count"] intValue];
        
        if (count > 1) {
            
            NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
            NSString *shopName = [uD objectForKey:@"shopName"];
//            [self setNavTitle:shopName];
            NSMutableAttributedString * attStr  = [NSMutableAttributedString new];
            [attStr appendAttributedString:[[NSAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@",shopName] attributes:nil]];

            ActImageTextAttachment * attachment1 = [ActImageTextAttachment new] ;
            attachment1.imgframe = CGRectMake(3, 0, 9, 9);
            attachment1.image = [UIImage imageNamed:@"triangle"];
            NSAttributedString * attachStr1 = [NSAttributedString attributedStringWithAttachment:attachment1];
            [attStr appendAttributedString:attachStr1];
            weakSelf.navTitleView.attributedText = attStr;
            
            
            UIButton * shopNameBut = [UIButton buttonWithType:UIButtonTypeCustom];
            shopNameBut.frame = CGRectMake(44, APP_STATUSBAR_HEIGHT, APP_VIEW_WIDTH-88, APP_NAVGATION_HEIGHT);
//            shopNameBut.backgroundColor = [UIColor blackColor];
            [shopNameBut addTarget:self action:@selector(shopNameButClick:) forControlEvents:UIControlEventTouchUpInside];
            [weakSelf.view addSubview:shopNameBut];
            

        }else{
            NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
            NSString *shopName = [uD objectForKey:@"shopName"];
            [self setNavTitle:shopName];
            return ;
        }

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];
    
    
}









//用户浏览量
-(void)gotoCountVC{
    BMSQ_CountViewController *vc = [[BMSQ_CountViewController alloc]init];
    vc.hidesBottomBarWhenPushed = YES;
    vc.count = self.countStr;
    [self.navigationController pushViewController:vc animated:YES];
}
//去入驻
-(void)gotoSetterVC{
    BMSQ_SettlerViewController *vc = [[BMSQ_SettlerViewController alloc]init];
    BaseNavViewController *nc = [[BaseNavViewController alloc]initWithRootViewController:vc];
    [self presentViewController:nc animated:YES completion:nil];
}
//我的订单
-(void)gotoOrderVC{
    
    NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
    NSString *isFood = [uD objectForKey:SHOPTYPE_FOOD];
    if ([isFood isEqualToString:@"1"]) {   //餐饮
        BMSQ_mineOrderViewController * mineVC = [[BMSQ_mineOrderViewController alloc] init];
        mineVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:mineVC animated:YES];
    }else{
        //非餐饮
        BMSQ_AwayOrderViewController * mineVC = [[BMSQ_AwayOrderViewController alloc] init];
        mineVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:mineVC animated:YES];
        
    }


}

#pragma mark ----- 推送提醒
- (void)shutDownButClick:(UIButton *)sender
{
    self.pushView.hidden = YES;
}



#pragma mark ---扫描---
- (void)btnRightClick
{
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    reader.showsHelpOnFail = NO;
    reader.scanCrop = CGRectMake(0.1, 0.2, 0.8, 0.8);
    
    ZBarImageScanner *scanner = reader.scanner;
    
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    UIView * view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    view.backgroundColor = [UIColor clearColor];
    reader.cameraOverlayView = view;
    
    UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, APP_VIEW_WIDTH - 40, 40)];
    label.text = @"请将扫描的二维码至于下面的框内！";
    label.textColor = [UIColor whiteColor];
    label.textAlignment = 1;
    label.lineBreakMode = 0;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
    [view addSubview:label];
    
    UIImageView * image = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"pick_bg.png"]];
    image.frame = CGRectMake(20, 80, APP_VIEW_WIDTH-40, APP_VIEW_WIDTH - 40);
    [view addSubview:image];
    
    
    _line = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, APP_VIEW_WIDTH - 100, 2)];
    _line.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [image addSubview:_line];
    //定时器，设定时间过1.5秒，
    timer = [NSTimer scheduledTimerWithTimeInterval:.01 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    
    
    [self presentViewController:reader animated:YES completion:nil];

    
}

-(void)animation1
{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (2*num == 260) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
    
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
    }];
}

- (void)imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    
    [reader dismissViewControllerAnimated:YES completion: nil];
    
    NSString *scanResult=symbol.data;
    if(scanResult==nil||scanResult.length==0){
        return ;
    }
 
    NSLog(@"%@", scanResult);
    
    
    
}





@end
