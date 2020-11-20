//
//  BMSQ_MyRecoCodeViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/18.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyRecoCodeViewController.h"
#import <ShareSDK/ShareSDK.h>
#import "SVProgressHUD.h"
#import "BMSQ_Share.h"
#import "MobClick.h"
@interface BMSQ_MyRecoCodeViewController ()
{
    NSDictionary *m_dic;
}

@property (nonatomic, strong)UILabel *exLabel;
@property (nonatomic, strong)UIScrollView *scrollView;



@end

@implementation BMSQ_MyRecoCodeViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"MyRecoCode"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"MyRecoCode"];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的推荐码"];
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    
    [self requestData];

   

}

-(void)initView:(NSDictionary *)dic{
 
    
    
    
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/7)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    float w = APP_VIEW_WIDTH/2;
    
    UIView *line1 = [[UIView alloc]initWithFrame:CGRectMake(w, 15, 1, topView.frame.size.height-30)];
    line1.backgroundColor = self.view.backgroundColor;
    [topView addSubview:line1];
    
    UIView *line2 = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-w, 15, 1, topView.frame.size.height-30)];
    line2.backgroundColor = self.view.backgroundColor;
    [topView addSubview:line2];
    
    UILabel *recomLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 10, w, topView.frame.size.height/2)];
    recomLabel.text = @"推荐人数/人";
    recomLabel.textColor = [UIColor blackColor];
    recomLabel.textAlignment = NSTextAlignmentCenter;
    recomLabel.font = [UIFont systemFontOfSize:15.f];
    [topView addSubview:recomLabel];
    
    UILabel *recomCount = [[UILabel alloc]initWithFrame:CGRectMake(0, recomLabel.frame.size.height, w, topView.frame.size.height/2)];
    recomCount.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"recomCount"]];
    recomCount.textColor = [UIColor blackColor];
    recomCount.textAlignment = NSTextAlignmentCenter;
    recomCount.font = [UIFont systemFontOfSize:25.f];
    [topView addSubview:recomCount];
    
    UILabel *bonusLabel = [[UILabel alloc]initWithFrame:CGRectMake(w, 10, w, topView.frame.size.height/2)];
    bonusLabel.text = @"奖励红包/元";
    bonusLabel.textColor = [UIColor blackColor];
    bonusLabel.textAlignment = NSTextAlignmentCenter;
    bonusLabel.font = [UIFont systemFontOfSize:15.f];
    [topView addSubview:bonusLabel];
    
    UILabel *bonusCount = [[UILabel alloc]initWithFrame:CGRectMake(w, recomLabel.frame.size.height, w, topView.frame.size.height/2)];
    bonusCount.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"bonusAmount"]];
    bonusCount.textColor = [UIColor blackColor];
    bonusCount.textAlignment = NSTextAlignmentCenter;
    bonusCount.font = [UIFont systemFontOfSize:25.f];
    [topView addSubview:bonusCount];
    
    UIButton *btn_bonus = [UIButton buttonWithType:UIButtonTypeCustom];
    btn_bonus.frame = CGRectMake(w, 10, w, topView.frame.size.height);
    
    
    
//    UILabel *couponLabel = [[UILabel alloc]initWithFrame:CGRectMake(w*2, 10, w, topView.frame.size.height/2)];
//    couponLabel.text = @"奖励优惠券/元";
//    couponLabel.textColor = [UIColor blackColor];
//    couponLabel.textAlignment = NSTextAlignmentCenter;
//    couponLabel.font = [UIFont systemFontOfSize:15.f];
//    [topView addSubview:couponLabel];
//    
//    UILabel *couponCount = [[UILabel alloc]initWithFrame:CGRectMake(w*2, recomLabel.frame.size.height, w, topView.frame.size.height/2)];
//    couponCount.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"couponAmount"]];
//    couponCount.textColor = [UIColor blackColor];
//    couponCount.textAlignment = NSTextAlignmentCenter;
//    couponCount.font = [UIFont systemFontOfSize:25.f];
//    [topView addSubview:couponCount];
    
    
    
    
    
    
    
    self.scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, topView.frame.origin.y+topView.frame.size.height, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-(topView.frame.origin.y+topView.frame.size.height))];
    [self.view addSubview:self.scrollView];
    
     w = APP_VIEW_WIDTH/2+20;
    
    
    UIImageView *redImageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 40, w, w/2)];
    [redImageView setImage:[UIImage imageNamed:@"recommended"]];
    [self.scrollView addSubview:redImageView];
    redImageView.center = CGPointMake(APP_VIEW_WIDTH/2, 70);
    
    CGFloat y=140;
    if (iPhone4) {
        y=130;
    }
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 40)];
    label.backgroundColor = [UIColor clearColor];
    [self.scrollView addSubview:label];
    label.text = @"邀请好友，赢惠圈红包";
    label.font = [UIFont fontWithName:@"Bradley Hand" size:23];
    label.textColor = [UIColor colorWithRed:74/255.0 green:74/255.0 blue:71/255.0 alpha:1];
    label.textAlignment = NSTextAlignmentCenter;
    
    CGSize size = [label.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-80, MAXFLOAT)
                                           options:NSStringDrawingUsesLineFragmentOrigin
                                        attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:23.f]}
                                           context:nil].size;
    
   
    y=190;
    if (iPhone4) {
        y=160;
    }
    UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-size.width)/2, y, size.width, 50)];
    label.backgroundColor = [UIColor clearColor];
    [self.scrollView addSubview:label1];
    label1.textColor = [UIColor colorWithRed:74/255.0 green:74/255.0 blue:71/255.0 alpha:1];
    label1.font = [UIFont systemFontOfSize:15];
    label1.numberOfLines =0;
    label1.textAlignment = NSTextAlignmentCenter;
    NSString *reward =[NSString stringWithFormat:@"%@", [dic objectForKey:@"reward"]] ;
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"成功邀请好友，推荐人可获%@元\n红包奖励，无封顶，无门槛",[dic objectForKey:@"reward"]]];
//    [str addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:NSMakeRange(19,reward.length)];
    label1.attributedText = str;
    
    
    
    y=250;
    if (iPhone4) {
        y=210;
    }
    UIView *recommenView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-(130/2), y, 150,80)];
    recommenView.layer.masksToBounds = YES;
    recommenView.layer.cornerRadius = 5;
    recommenView.layer.borderColor = [[UIColor grayColor]CGColor];
    recommenView.layer.borderWidth = 0.7;
    recommenView.backgroundColor = [UIColor whiteColor];
    [self.scrollView addSubview:recommenView];
    
    
    UILabel *inviteCodelabel1 = [[UILabel alloc]initWithFrame:CGRectMake(0, 10, recommenView.frame.size.width, 30)];
    inviteCodelabel1.backgroundColor = [UIColor clearColor];
    inviteCodelabel1.text = @"发送您的推荐码";
    inviteCodelabel1.font = [UIFont systemFontOfSize:15];
    inviteCodelabel1.numberOfLines =2;
    inviteCodelabel1.textColor = [UIColor colorWithRed:74/255.0 green:74/255.0 blue:71/255.0 alpha:1];
    inviteCodelabel1.textAlignment = NSTextAlignmentCenter;
    [recommenView addSubview:inviteCodelabel1];
    
    UILabel *inviteCode = [[UILabel alloc]initWithFrame:CGRectMake(0,inviteCodelabel1.frame.size.height+inviteCodelabel1.frame.origin.y, recommenView.frame.size.width, 30)];
    inviteCode.backgroundColor = [UIColor clearColor];
    inviteCode.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"inviteCode"]];
    inviteCode.font = [UIFont fontWithName:@"Arial" size:20];
    inviteCode.numberOfLines =2;
    inviteCode.textColor = [UIColor colorWithRed:74/255.0 green:74/255.0 blue:71/255.0 alpha:1];
    inviteCode.textAlignment = NSTextAlignmentCenter;
    [recommenView addSubview:inviteCode];
    y=350;
    if (iPhone4) {
        y=300;
    }
    UIButton *shareButton = [[UIButton alloc]initWithFrame:CGRectMake(30, y, APP_VIEW_WIDTH-60, 40)];
    shareButton.backgroundColor = [UIColor colorWithRed:182/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    shareButton.layer.masksToBounds = YES;
    shareButton.layer.cornerRadius = 5;
    [shareButton setTitle:@"分享给好友" forState:UIControlStateNormal];
    [shareButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [shareButton addTarget:self action:@selector(btnShareClick) forControlEvents:UIControlEventTouchUpInside];
    shareButton.titleLabel.font = [UIFont systemFontOfSize:16];
    [self.scrollView addSubview:shareButton];
    
    UILabel *left = [[UILabel alloc]initWithFrame:CGRectMake(0, shareButton.frame.origin.y+shareButton.frame.size.height+40, (APP_VIEW_WIDTH-100)/2, 0.5)];
    left.backgroundColor = [UIColor colorWithRed:167/255.0 green:0 blue:0 alpha:1];
    [self.scrollView addSubview:left];
    
    UILabel *right = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-(APP_VIEW_WIDTH-100)/2, shareButton.frame.origin.y+shareButton.frame.size.height+40, (APP_VIEW_WIDTH-100)/2, 0.5)];
    right.backgroundColor = [UIColor colorWithRed:167/255.0 green:0 blue:0 alpha:1];
    [self.scrollView addSubview:right];
    
    UILabel *center = [[UILabel alloc]initWithFrame:CGRectMake(10+left.frame.size.width, left.frame.origin.y-10, 80, 20)];
    center.backgroundColor = [UIColor clearColor];
    center.layer.borderColor = [[UIColor colorWithRed:167/255.0 green:0 blue:0 alpha:1]CGColor];
    center.layer.borderWidth=0.5;
    center.layer.masksToBounds = YES;
    center.layer.cornerRadius = 3;
    center.textAlignment = NSTextAlignmentCenter;
    center.font = [UIFont boldSystemFontOfSize:13.f];
    center.textColor =[UIColor colorWithRed:167/255.0 green:0 blue:0 alpha:1];
    center.text = @"推荐规则";
    [self.scrollView addSubview:center];
    
    UIButton *extendButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 40, 20)];
    [extendButton setImage:[UIImage imageNamed:@"exUp"] forState:UIControlStateNormal];
    [extendButton setImage:[UIImage imageNamed:@"exDown"] forState:UIControlStateSelected];
    extendButton.center = CGPointMake(APP_VIEW_WIDTH/2, center.frame.origin.y+50);
    [extendButton addTarget:self action:@selector(exShow:) forControlEvents:UIControlEventTouchUpInside];
    [self.scrollView addSubview:extendButton];
    
    
//    1.被邀请人必须是惠圈新用户，未注册过惠圈。
//    2.被邀请人点击注册页面，输入您的邀请码，您将获得惠圈1元红包，红包可抵现金累计使用
//    3.被邀请人首次成功消费后，您还将获得4元优惠券作为奖励。
//    4.被邀请人完成一次下单支付操作视为一次成功消费。
//    5.使用非正常途径或手段获得的优惠券无效，惠圈保留最终解释权。
    
    self.exLabel = [[UILabel alloc]initWithFrame:CGRectMake(20,  center.frame.origin.y + 80, APP_VIEW_WIDTH-40, 200)];
//    self.exLabel.text = @"1、推荐好友下载“惠圈”APP，并使用本人邀请码进行注册，签约工行发行的银行卡快捷支付即可获得10元奖励；\n2、推荐好友数量不限，每个注册用户只能使用一个邀请码；\n3、同一手机、同一惠圈账号、同一签约的工行卡视为同一用户；\n4、所获奖励可以在惠圈上所有商户进行消费，但必须使用工行快捷支付，实际支付金额不能低于1元；\n6、好友安装并使用惠圈APP时所在地须在惠圈开通城市；\n 7、推荐好友所获得奖励不能用于出售、交换等其他通途；\n 8、如果您的惠圈账号存在异常或被终止服务，则无法继续使用惠圈账户内的零钱；\n9、本规则最终解释权归惠圈所有";
    self.exLabel.text = [dic objectForKey:@"rules"];
    self.exLabel.font = [UIFont systemFontOfSize:11.f];
    self.exLabel.numberOfLines = 0;
    self.exLabel.textColor = [UIColor colorWithRed:78/255.0 green:77/255.0 blue:72/255.0 alpha:1];
    [self.scrollView addSubview:self.exLabel];
    
    size = [self.exLabel.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-40, MAXFLOAT)
                                    options:NSStringDrawingUsesLineFragmentOrigin
                                 attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.f]}
                                    context:nil].size;
    self.exLabel.frame = CGRectMake(20, center.frame.origin.y+70, size.width, size.height);
    self.exLabel.hidden = YES;
    
    [self.scrollView setContentSize:CGSizeMake(APP_VIEW_WIDTH, center.frame.origin.y+80)];
    
}
-(void)exShow:(UIButton *)button{
    
    button.selected = !button.selected;
    if (button.selected) {
        CGSize size = self.scrollView.contentSize;
        self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, size.height+self.exLabel.frame.size.height);
        self.exLabel.hidden =NO;
        
    }else{
        CGSize size = self.scrollView.contentSize;
        self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, size.height-self.exLabel.frame.size.height);
        self.exLabel.hidden = YES;

        
    }
}


-(void)requestData{
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
   [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getUserInviteCode" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [ProgressManage openProgressText:nil];
    
    [self.jsonPrcClient invokeMethod:@"getUserInviteCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        m_dic = responseObject;
        [wself initView:responseObject];
        

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];

}


- (void)btnShareClick
{
    //创建分享内容
    NSString *title=[NSString stringWithFormat:@"用我的推荐码注册惠圈，首单立减%@元",[m_dic objectForKey:@"reward"]];
    NSString* remark = @"成功邀请好友送红包，上不封顶，想拿多少就拿多少，可叠加使用！";
    NSString* url = [NSString stringWithFormat:@"%@/Index/invitationCodeShare/code/%@",BASE_URL,[m_dic objectForKey:@"inviteCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    [BMSQ_Share shareClick:remark imagePath:imagePath title:title url:url];
}

@end
