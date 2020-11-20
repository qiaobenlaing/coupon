//
//  BMSQ_educationViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/9.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_educationViewController.h"
#import "MobClick.h"
//cell
#import "benTopView.h"
#import "BMSQ_shopImageCell.h"
#import "EductionSchedule.h"
#import "ScheduleStr.h"
#import "EducationCell.h"
#import "BMSQ_RecommendShopCell.h"
//#import "NewMyCouponViewCell.h"

#import "ShopCouponCell.h"
#import "Benefit_activityCell.h"

//VC
#import "BMSQ_educationTeacherListViewController.h"
#import "BMSQ_TeaDetailViewController.h"
#import "BMSQ_RecruitmentViewController.h"
#import "BMSQ_CourseContentViewController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_VisitorViewController.h"


#import "BMSQ_PayDetailSViewController.h"
typedef enum{
    
    UITableViewTypeMain,                //首页
    UITableViewTypeActvity,             //优惠/活动
    UITableViewTypeSchedule,            //课程表
    UITableViewTypeHonorWall,           //荣誉墙
    uitableViewTypeLanguagePresidents   //校长之语
    
}UITableViewShowType;


@interface BMSQ_educationViewController () <UITableViewDataSource,UITableViewDelegate,benTopViewDelegate,BMSQ_shopImageCellDelegate,EductionScheduleDelegate,ShopCouponCellDelegate>
{
    
    benTopView *sectionCell;
}

@property (nonatomic, assign)UITableViewShowType tableViewType;
@property (nonatomic, strong)UITableView *mainTableView;
@property (nonatomic, strong)UIButton *bottomBtn;
@property (nonatomic, strong)NSArray *actList;//商家活动
@property (nonatomic, strong)NSArray *aroundShop;//附近商家
@property (nonatomic, strong)NSArray *shopCoupon;//商家优惠券
@property (nonatomic, strong)NSArray *userCoupon;//用户优惠券
@property (nonatomic, strong)NSArray *recentVisitor;//最近访问的用户
@property (nonatomic, strong)NSArray *shopClass;//课程表
@property (nonatomic, strong)NSArray *shopDecoration;//商家环境图片
@property (nonatomic, strong)NSDictionary *shopHeader;//校长之语
@property (nonatomic, strong)NSArray *shopHonor;//商家荣誉墙
@property (nonatomic, strong)NSDictionary *shopInfo;//商店信息
@property (nonatomic, strong)NSArray *shopPhotoList;//商家产品图片
@property (nonatomic, strong)NSDictionary *shopRecruitInfo;//商家招生启事
@property (nonatomic, strong)NSArray *shopTeacher;//名师堂
@property (nonatomic, strong)NSDictionary *studentStar;//每日/周/月之星



@property (nonatomic, assign)BOOL isSHowAct;  //
@property (nonatomic, assign)BOOL isSHowUserCoupon;
@property (nonatomic, assign)BOOL isSHowShopCoupon;


@property (nonatomic, assign)BOOL isHaveData;


@property (nonatomic, strong)EducationCell *mainCell;

@end

@implementation BMSQ_educationViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"educationViewController"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"educationViewController"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavBackGroundColor:[UIColor clearColor]];
    UIImageView *navImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y)];
    [navImage setImage:[UIImage imageNamed:@"app_nav"]];
    navImage.tag = 999;
    [self.navigationView addSubview:navImage];
    [self.navigationView  sendSubviewToBack:navImage];
    
    
    self.isSHowAct = YES;
    self.isSHowShopCoupon = YES;
    self.isSHowUserCoupon = YES;
    

    //点赞 取消赞
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(clickTeacher:) name:@"clickTeacher" object:nil];
    //取消赞
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(cancelClickTeacher:) name:@"cancelClickTeacher" object:nil];
    //登录
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(userlogin) name:@"userlogin" object:nil];
    //名师详情
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(gotoTeaDetailVC:) name:@"gotoTeaDetailVC" object:nil];

    self.isHaveData = NO;
    UIButton *sharebtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-120, 20,  APP_VIEW_ORIGIN_Y-20, APP_VIEW_ORIGIN_Y-20)];
    [sharebtn setImage:[UIImage imageNamed:@"icon_share"] forState:UIControlStateNormal];
    [sharebtn addTarget:self action:@selector(clickShare) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:sharebtn];
    
    
    UIButton *btn1 = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 20,  APP_VIEW_ORIGIN_Y-20, APP_VIEW_ORIGIN_Y-20)];
    [btn1 setImage:[UIImage imageNamed:@"icon_chat"] forState:UIControlStateNormal];
    [btn1 addTarget:self action:@selector(clickMessage) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btn1];
    
    
    self.mainCell = [[EducationCell alloc]init];
    
    self.tableViewType = UITableViewTypeMain;
    self.mainTableView = [[UITableView alloc]initWithFrame:CGRectMake(0,0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.mainTableView.backgroundColor = UICOLOR(239, 239, 244, 1);
    self.mainTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.mainTableView.delegate = self;
    self.mainTableView.dataSource = self;
    [self.view insertSubview:self.mainTableView belowSubview:self.navigationView];
    
    self.bottomBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-50, APP_VIEW_WIDTH, 50)];
    [self.bottomBtn setTitle:@"使用惠支付" forState:UIControlStateNormal];
    self.bottomBtn.backgroundColor = APP_NAVCOLOR;
    [self.bottomBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.bottomBtn.layer.cornerRadius=6;
    self.bottomBtn.layer.masksToBounds = YES;
    self.bottomBtn.alpha = 0.613;
//    [self.view addSubview:self.bottomBtn];
    
    
    
    [self getShopInfo];
    
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        static NSString *identifier = @"shopImageCell";
        BMSQ_shopImageCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell ==nil) {
            cell = [[BMSQ_shopImageCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.shopHeadDelegate = self;
        }
        [cell setShopImageCell:self.shopDecoration shopInfo:self.shopInfo :APP_VIEW_HEIGHT/2-50];
        return cell;
    }else if(self.tableViewType == UITableViewTypeMain){
        
         return  [self.mainCell setMainCell:tableView indexPath:indexPath];
        
    }

    else if(self.tableViewType == UITableViewTypeActvity){
        
        if ( indexPath.section ==2) {   //我的优惠券
            static NSString * cell_id = @"shopCouponCell";
            ShopCouponCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[ShopCouponCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                cell.backgroundColor = APP_VIEW_BACKCOLOR;
                cell.couponDelegate = self;
                cell.selectionStyle=UITableViewCellSelectionStyleNone;
                
            }
            
            NSDictionary *couponDic = [self.userCoupon objectAtIndex: indexPath.row];
            cell.couponDic =couponDic;

            NSString *remark = [couponDic objectForKey:@"remark"];
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11]}
                                               context:nil].size;
            
            float height = 250+(size.height<=15?0:size.height);
            [cell setShopCouponCell:self.userCoupon[indexPath.row] heigh:height status:YES];
            
            return cell;
        }else if (indexPath.section ==3){  //未领取的优惠券

            static NSString * cell_id = @"shopCouponCell";
            ShopCouponCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[ShopCouponCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                cell.backgroundColor = APP_VIEW_BACKCOLOR;
                cell.couponDelegate = self;
                cell.selectionStyle=UITableViewCellSelectionStyleNone;
                
            }
            
            
            NSDictionary *couponDic = [self.shopCoupon objectAtIndex: indexPath.row];
            cell.couponDic =couponDic;

            NSString *remark = [couponDic objectForKey:@"remark"];
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11]}
                                               context:nil].size;
            
            float height = 250+(size.height<=15?0:size.height);
            [cell setShopCouponCell:self.shopCoupon[indexPath.row] heigh:height status:NO];

            return cell;
        }else if (indexPath.section ==4){  //商家活动
            
            static NSString * cell_id = @"Benefit_activityCellsss";
            Benefit_activityCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[Benefit_activityCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            NSDictionary *dic = [self.actList objectAtIndex:indexPath.row];
            [cell initActivityCell:dic];
            return cell;
        
        }
   

    }
    else if (self.tableViewType == UITableViewTypeSchedule) {
        if (indexPath.section ==2) {
            static NSString *identifier = @"ScheduleCell";
            EductionSchedule *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[EductionSchedule alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                cell.schDelegate = self;
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
            }
            
            NSDictionary *schDic = [self.shopClass objectAtIndex:indexPath.row];
            float w1 =(APP_VIEW_WIDTH-18)/5*2;
            NSDictionary *dic = [ScheduleStr scheduleStr:schDic w:w1];
            NSString *height = [dic objectForKey:@"height"];
            [cell setSchedule:[self.shopClass objectAtIndex:indexPath.row] heigh: [height floatValue]];
            return cell;
        }
        
        
    }
    else if (self.tableViewType ==UITableViewTypeHonorWall){
        
        if (indexPath.section ==1) {
            
            static NSString *identifier = @"HonorWallCell";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                UIImageView *bgImageView = [[UIImageView alloc]initWithFrame:CGRectMake(9, 6, APP_VIEW_WIDTH-18, 120-6)];
                bgImageView.backgroundColor = [UIColor whiteColor];
                bgImageView.tag = 100;
                [bgImageView setImage:[UIImage imageNamed:@"iv_noData"]];
                [cell addSubview:bgImageView];
                
            }
            UIImageView *honorImage = (UIImageView *)[cell viewWithTag:100];
            NSDictionary *dic = [self.shopHonor objectAtIndex:indexPath.row];
            NSString *honorUrl = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"honorUrl"]];
            [honorImage sd_setImageWithURL:[NSURL URLWithString:honorUrl] placeholderImage:[UIImage imageNamed:@"iv_noData"]];
            return cell;
        }

    }
    else if (self.tableViewType == uitableViewTypeLanguagePresidents){
        
        if (indexPath.section ==1) {
            
            static NSString *identifier = @"LanguagePresidentsCell";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell ==nil) {
                //200
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                UIImageView *bgImageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 6, APP_VIEW_WIDTH, 100)];
                bgImageView.backgroundColor = [UIColor whiteColor];
                [bgImageView setImage:[UIImage imageNamed:@"iv_noData"]];
                bgImageView.tag = 100;
                [cell addSubview:bgImageView];
                UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(6, 106, APP_VIEW_WIDTH-12, 200-106)];
                label.numberOfLines = 0;
                label.textColor = APP_TEXTCOLOR;
                label.font = [UIFont systemFontOfSize:12];
                label.tag =200;
                [cell addSubview:label];
            }
//            expModel = 2;
//            imgUrl = "/Public/Uploads/20160315/56e7b5948e4d6.png";
//            txtMemo = "\U590d\U53e4\U7684\U98ce\U683c\U5fb7\U56fd\U53cd\U5bf9\U6539\U9769\U7684\U75af\U75af\U766b\U766b";
            UIImageView *honorImage = (UIImageView *)[cell viewWithTag:100];
            NSString *honorUrl = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.shopHeader objectForKey:@"imgUrl"]];
            [honorImage sd_setImageWithURL:[NSURL URLWithString:honorUrl] placeholderImage:[UIImage imageNamed:@"iv_noData"]];
            UILabel *label = (UILabel *)[cell viewWithTag:200];
            label.text = [self.shopHeader objectForKey:@"txtMemo"];
            return cell;

        }

        
    }
    
    
    static NSString *identifier = @"allkkjhlkjlkj";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];

    }
    cell.textLabel.text = [NSString stringWithFormat:@"row = %d",(int)indexPath.row];
    return cell;
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section ==0) {
        return  APP_VIEW_HEIGHT/2-50;
    }
    else if ( self.tableViewType==UITableViewTypeMain){
        
        if (indexPath.section ==2) { //地址
            return 55;
        }else if (indexPath.section ==3){ //最近访问
            return 50;
        }else if (indexPath.section ==4){ //名师堂
            float spx =((APP_VIEW_WIDTH-10)/3)/4;
            float w = (APP_VIEW_WIDTH-spx*4)/3;
            return w+70+50+10;
        }else if (indexPath.section ==5){ //每日之星
            return 35;
        }else if (indexPath.section ==6){ //每日之星内容
            
            NSString *starInfo=[self.studentStar objectForKey:@"starInfo"];
            CGSize size = [starInfo boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-16, MAXFLOAT)
                                                 options:NSStringDrawingUsesLineFragmentOrigin
                                              attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13]}
                                                 context:nil].size;
            
            return 100+10+(size.height+20);
        }else if (indexPath.section ==7){ //招生启示
            return 35;
        }else if (indexPath.section ==8){ //招生启示内容
            return 100+10;
        } else if (indexPath.section ==9){ //推荐商家
            if(indexPath.row == self.aroundShop.count-1){
                return [BMSQ_RecommendShopCell cellHeigh:[self.aroundShop objectAtIndex:indexPath.row]]+100;
            }
            return [BMSQ_RecommendShopCell cellHeigh:[self.aroundShop objectAtIndex:indexPath.row]];;
        }else{
            return 0;
        }

        
    }
    else if ( self.tableViewType==UITableViewTypeActvity){
        if(indexPath.section ==2 ){ //优惠已领取
            
            NSDictionary *couponDic = [self.userCoupon objectAtIndex: indexPath.row];
            NSString *remark = [couponDic objectForKey:@"remark"];
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                               options:NSStringDrawingUsesLineFragmentOrigin
                                            attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11]}
                                               context:nil].size;
            
            return 250 + (size.height<=15?0:size.height);
        
        }else if (indexPath.section ==3){ //优惠未领取
            
            NSDictionary *couponDic = [self.shopCoupon objectAtIndex: indexPath.row];
            NSString *remark = [couponDic objectForKey:@"remark"];
            CGSize size = [remark boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20,MAXFLOAT)
                                                              options:NSStringDrawingUsesLineFragmentOrigin
                                                           attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11]}
                                                              context:nil].size;
            
            return 250 + (size.height<=15?0:size.height);
        }else if (indexPath.section ==4){ //活动
            return 140;
        }
    }
    else if (self.tableViewType == UITableViewTypeSchedule){
        if (indexPath.section ==2) {
            
            
            NSDictionary *schDic = [self.shopClass objectAtIndex:indexPath.row];
            float w1 =(APP_VIEW_WIDTH-18)/5*2;
            NSDictionary *dic = [ScheduleStr scheduleStr:schDic w:w1];
            NSString *height = [dic objectForKey:@"height"];
            return [height floatValue];
            
            //课程内容高度

        }
    }
    else if (self.tableViewType == UITableViewTypeHonorWall){
        if (indexPath.section ==1) {
            return 120;    //荣誉照片
        }
    }else if (self.tableViewType == uitableViewTypeLanguagePresidents){
        if (indexPath.section==1) {
            return 200;
        }
    }
    
    return 0;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (!self.isHaveData) {
        return 0;
    }
    if (section ==0) {
        return 1;
    }
    else if (self.tableViewType ==UITableViewTypeMain){
        if ( section ==9){   //推荐商家
            return self.aroundShop.count;
        }else{
            //名师堂
            if (section == 4){
                if (self.shopTeacher.count>0) {
                    return 1;
                }else{
                    return 0;
                }
            }//每日之星  每日之星内容
            else if (section == 5 ||section == 6){
              
                if ([self.studentStar objectForKey:@"starCode"]) {
                    NSString *starCode =[self.studentStar objectForKey:@"starCode"];
                    if (starCode.length>0) {
                        return 1;
                    }else{
                        return 0;
                    }
                }else{
                    return 0;
                }
                
            }  //招生启示  招生启示内容
            else if(section == 7 ||section == 8){
                if ([self.shopRecruitInfo objectForKey:@"recruitCode"]) {
                    NSString *recruitCode =[self.shopRecruitInfo objectForKey:@"recruitCode"];
                    if (recruitCode.length>0) {
                        return 1;
                    }else{
                        return 0;
                    }
                }else{
                    return 0;
                }

            }
          
            return 1;
        }
        
    }
    else if (self.tableViewType==UITableViewTypeActvity ){

        if( section ==2){   //优惠已领取
            if (self.isSHowUserCoupon) {
                return self.userCoupon.count;
            }else{
                return 0;
            }
            
        }else if (section ==3){   //优惠券未领取
            if (self.isSHowShopCoupon) {
                return self.shopCoupon.count;

            }else{
                return 0;

            }
            
        }else if (section ==4){   //商家活动
            if (self.isSHowAct) {
                return self.actList.count;

            }else{
                return 0;
            }

        }
    }
    else if (self.tableViewType==UITableViewTypeSchedule){
        if (section == 2) {
            return self.shopClass.count;       //课程内容
        }
    }else if (self.tableViewType==UITableViewTypeHonorWall){
        if (section ==1) {
            return self.shopHonor.count;     //荣誉墙
        }
    }else if (self.tableViewType == uitableViewTypeLanguagePresidents){
        if (section==1) {
            return 1;
        }
    }
    return 0;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{

    switch (self.tableViewType) {
        case  UITableViewTypeMain:
        {
            return 10;  //图片 选项卡 地址 最近访问 名师堂   每日之星 每日之星信息 招生启示 招生启示信息  附近商家
        }
            break;
        case  UITableViewTypeActvity:
        {
            return 5; //图片  选项卡 我的优惠券  未领取优惠券  活动赛事
        }
            break;
        case  UITableViewTypeSchedule:
        {
            return 3;//图片  选项卡  课程表
        }
            break;
        case  UITableViewTypeHonorWall:
        {
            return 2;//图片  选项卡(荣誉墙)
        }
            break;
        case  uitableViewTypeLanguagePresidents:
        {
            return 2;//图片  选项卡(校长之语)
        }
            break;
            
        default:
        {
            return 0;
        }
            break;
    }
    
}
-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{

    if (section ==1) {
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 60)];
        bgView.backgroundColor = UICOLOR(239, 239, 244, 1);

        if (sectionCell==nil) {
            NSArray *array =@[@"首页",@"优惠/活动",@"课程表",@"荣誉墙",@"校长之语"];
            sectionCell = [[benTopView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 50)];
            sectionCell.isHiddenLine = YES;
            [sectionCell createTopViewtitleArray:array];
            sectionCell.typeDelegate = self;
            [bgView addSubview:sectionCell];
            
            return bgView;

        }
        sectionCell.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, 50);
        [bgView addSubview:sectionCell];

        return bgView;
        
    }
    else if (self.tableViewType ==UITableViewTypeMain ){
        if ( section ==9) {
        
            
            UILabel *bgView = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
            bgView.backgroundColor = [UIColor whiteColor];
            bgView.text = @"   附近其他商家";
            bgView.textColor = APP_TEXTCOLOR;
            bgView.font = [UIFont boldSystemFontOfSize:14.f];
            
            
            
            return bgView;
        }
     }
    else if (self.tableViewType==UITableViewTypeActvity){
         
         if ( section ==2) {
        
             UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             bgView.backgroundColor = [UIColor whiteColor];
             
             UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             titleLabel.backgroundColor = [UIColor whiteColor];
             titleLabel.text =  @"  我的优惠券";
             titleLabel.textColor = APP_TEXTCOLOR;
             titleLabel.font = [UIFont boldSystemFontOfSize:14.f];
             [bgView addSubview:titleLabel];
             
             UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             [button setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
             [button setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];
             button.backgroundColor = [UIColor clearColor];
             button.tag = 2000;
             button.selected = self.isSHowUserCoupon;
             button.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, -(APP_VIEW_WIDTH/2+100));

             [button addTarget:self action:@selector(clickShowButton:) forControlEvents:UIControlEventTouchUpInside];
             [bgView addSubview:button];
             
             return bgView;
             
         }else if (section ==3){
             
             UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             bgView.backgroundColor = [UIColor whiteColor];
             
             UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             titleLabel.backgroundColor = [UIColor whiteColor];
             titleLabel.text = @"  未领取的优惠券";
             titleLabel.textColor = APP_TEXTCOLOR;
             titleLabel.font = [UIFont boldSystemFontOfSize:14.f];
             [bgView addSubview:titleLabel];
             
             UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             [button setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
             [button setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];
             button.backgroundColor = [UIColor clearColor];
             button.selected = self.isSHowShopCoupon;
             button.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, -(APP_VIEW_WIDTH/2+100));
             button.tag = 2001;
             [button addTarget:self action:@selector(clickShowButton:) forControlEvents:UIControlEventTouchUpInside];
             [bgView addSubview:button];

             return bgView;

         }else if (section ==4){
             
             UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             bgView.backgroundColor = [UIColor whiteColor];
             
             UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             titleLabel.backgroundColor = [UIColor whiteColor];
             titleLabel.text = @"  商家活动";
             titleLabel.textColor = APP_TEXTCOLOR;
             titleLabel.font = [UIFont boldSystemFontOfSize:14.f];
             [bgView addSubview:titleLabel];
             
             UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
             [button setImage:[UIImage imageNamed:@"garydown"] forState:UIControlStateNormal];
             [button setImage:[UIImage imageNamed:@"garyup"] forState:UIControlStateSelected];
             button.backgroundColor = [UIColor clearColor];
             button.tag = 2002;
             button.selected = self.isSHowAct;
             button.imageEdgeInsets = UIEdgeInsetsMake(0, 0, 0, -(APP_VIEW_WIDTH/2+100));

             [button addTarget:self action:@selector(clickShowButton:) forControlEvents:UIControlEventTouchUpInside];
             [bgView addSubview:button];
             
             return bgView;


         }
         
    }
    else if(self.tableViewType==UITableViewTypeSchedule){
        if (section == 2) {
            UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH,40)];
            bgView.backgroundColor = [UIColor clearColor];
            UIView *mainView = [[UIView alloc]initWithFrame:CGRectMake(9, 4, APP_VIEW_WIDTH-18, 35)];
            mainView.backgroundColor = [UIColor whiteColor];
            [bgView addSubview:mainView];
            float w = (APP_VIEW_WIDTH-18)/5;
            float w1 =(APP_VIEW_WIDTH-18)/5*2;

            NSArray *array = @[@"所开班级",@"上课时间",@"任课教师",@"查看详情"];
            
            
            float x =0;

            for (int i=0; i<array.count; i++) {
                
                UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(x, 0, i==1?w1:w, 35)];
                label.text = [array objectAtIndex:i];
                label.textAlignment = NSTextAlignmentCenter;
                label.font = [UIFont systemFontOfSize:12];
                label.textColor = APP_TEXTCOLOR;
                [mainView addSubview:label];
                
                if (i==1) {
                    x=x+w1;
                }else{
                    x=x+w;
                }
            }
            
             x =w;
            for (int i=1; i<4; i++) {
              
                UIView *lineV = [[UIView alloc]initWithFrame:CGRectMake(x, 0, 1, 35)];
                lineV.backgroundColor = self.view.backgroundColor;
                [mainView addSubview:lineV];
                if (i==1) {
                    x=x+w1;
                }else{
                    x=x+w;
                }
                
                
            }
          
            
            
            
            return bgView;
            
        }
    }
    return nil;
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    
    if (section ==1) {
        return 60;
    }
    else if (self.tableViewType ==UITableViewTypeMain){
        if (section ==9) {
            
            if (self.aroundShop.count>0) {
                return 35;  //推荐商家

            }else{
                return 0;
            }
        }
    }
    else if (self.tableViewType==UITableViewTypeActvity){
        
//        @property (nonatomic, strong)NSArray *shopCoupon;//商家优惠券
//        @property (nonatomic, strong)NSArray *userCoupon;//用户优惠券
        
        if (section ==2) {
            
            if (self.userCoupon.count>0) {
                return 40;  //我的优惠券

            }else{
                return 0;
            }
        }
        else if ( section ==3){
            if (self.shopCoupon.count>0) {
                return 40;
            }else{
                return 0;  //未领取优惠券

            }
        }
        else if ( section ==4){
            if (self.actList.count>0) {
                return 40;  //商家活动

            }else{
                return 0;
            }
        }
    }
    else if (self.tableViewType == UITableViewTypeSchedule){
        if (section ==2) {
            return 40;
        }
    }
    return 0;
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (self.tableViewType == UITableViewTypeMain) {
        if(indexPath.section == 3){
            BMSQ_VisitorViewController * visitorVC = [[BMSQ_VisitorViewController alloc] init];
            visitorVC.visitorAry = [NSArray arrayWithArray:self.recentVisitor];
            [self.navigationController pushViewController:visitorVC animated:YES];
        }else  if (indexPath.section ==4) {   //名师堂
                 BMSQ_educationTeacherListViewController *vc = [[BMSQ_educationTeacherListViewController alloc]init];
                vc.shopCode = [self.shopInfo objectForKey:@"shopCode"];
                [self.navigationController pushViewController:vc animated:YES];
            }
            else if (indexPath.section==5 ||indexPath.section ==6){
                BMSQ_TeaDetailViewController *vc = [[BMSQ_TeaDetailViewController alloc]init];
                vc.teaTitel = [self.studentStar objectForKey:@"starName"];
                vc.isTeacher = NO;
                vc.starCode = [self.studentStar objectForKey:@"starCode"];
                [self.navigationController pushViewController:vc animated:YES];
            }
            else if (indexPath.section==7||indexPath.section==8){
                BMSQ_RecruitmentViewController *vc = [[BMSQ_RecruitmentViewController alloc]init];
                vc.shopRecruitInfo = self.shopRecruitInfo;
                vc.shopCode = [self.shopInfo objectForKey:@"shopCode"];
                [self.navigationController pushViewController:vc animated:YES];
            }else if (indexPath.section ==9){
                NSDictionary *dic = [self.aroundShop objectAtIndex:indexPath.row];
                
                //isCatering 0-未知，1-餐饮，2-教育
                int isCatering = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"isCatering"]]intValue];
                
                if (isCatering == 2) {
                    BMSQ_educationViewController *vc = [[BMSQ_educationViewController alloc]init];
                    vc.shopCode = [dic objectForKey:@"shopCode"];
                    [self.navigationController pushViewController:vc animated:YES];
                }else {
                    BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
                    detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
                    detailCtrl.userCode = [gloabFunction getUserCode];
                    [self.navigationController pushViewController:detailCtrl animated:YES];
                    
                }

            
            
            }
    }
    else if (self.tableViewType == UITableViewTypeSchedule) {
        
        if (indexPath.section ==2) {
            
            BMSQ_CourseContentViewController *vc = [[BMSQ_CourseContentViewController alloc]init];
            NSDictionary *dic = [self.shopClass objectAtIndex:indexPath.row];
            vc.classCode = [dic objectForKey:@"classCode"];
            vc.shopCode = [self.shopInfo objectForKey:@"shopCode"];
            vc.beforeClassInfo = dic;
            vc.className = [dic objectForKey:@"className"];
            [self.navigationController pushViewController:vc animated:YES];
        }
        
    }
    else if (self.tableViewType ==UITableViewTypeActvity) {
        if(indexPath.section ==2 ){ //优惠已领取
            
            NSDictionary *dic =[self.userCoupon objectAtIndex:indexPath.row];
            BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
            couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
//            couponVC.CouponMessage = cell.couponMessage;
            couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[dic objectForKey:@"batchNbr"]];
            [self.navigationController pushViewController:couponVC animated:YES];
            

        
        
        }else if (indexPath.section ==3){ //优惠未领取
            
            NSDictionary *dic =[self.shopCoupon objectAtIndex:indexPath.row];
            BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
            couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
//            couponVC.CouponMessage = cell.couponMessage;
            couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[dic objectForKey:@"batchNbr"]];
            [self.navigationController pushViewController:couponVC animated:YES];
            

        }else if (indexPath.section ==4){ //活动

            NSDictionary *dic = [self.actList objectAtIndex:indexPath.row];
            NSString *activityCode =[dic objectForKey:@"activityCode"];
            BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
            vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];

        
        
        }
    }
    
    
    
    
}

#pragma mark -  商店详情
- (void)getShopInfo
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopInfo" strParams:self.shopCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getShopInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        weakSelf.actList = [responseObject objectForKey:@"actList"];
        weakSelf.aroundShop =[responseObject objectForKey:@"aroundShop"];
        NSDictionary *couponList = [responseObject objectForKey:@"couponList"];
        weakSelf.shopCoupon = [couponList objectForKey:@"shopCoupon"];
        weakSelf.userCoupon = [couponList objectForKey:@"userCoupon"];
        weakSelf.recentVisitor = [responseObject objectForKey:@"recentVisitor"];
        weakSelf.shopClass = [responseObject objectForKey:@"shopClass"];
        weakSelf.shopDecoration = [responseObject objectForKey:@"shopDecoration"];
        weakSelf.shopHeader = [responseObject objectForKey:@"shopHeader"];
        weakSelf.shopHonor = [responseObject objectForKey:@"shopHonor"];
        weakSelf.shopInfo = [responseObject objectForKey:@"shopInfo"];
        weakSelf.shopPhotoList = [responseObject objectForKey:@"shopPhotoList"];
        weakSelf.shopRecruitInfo = [responseObject objectForKey:@"shopRecruitInfo"];
        weakSelf.shopTeacher = [responseObject objectForKey:@"shopTeacher"];
        weakSelf.studentStar = [responseObject objectForKey:@"studentStar"];
        weakSelf.isHaveData = YES;
        
        
        weakSelf.mainCell.shopInfo =[responseObject objectForKey:@"shopInfo"];
        weakSelf.mainCell.shopTeacher =[responseObject objectForKey:@"shopTeacher"];
        weakSelf.mainCell.shopRecruitInfo =[responseObject objectForKey:@"shopRecruitInfo"];
        weakSelf.mainCell.studentStar =[responseObject objectForKey:@"studentStar"];
        weakSelf.mainCell.aroundShop =[responseObject objectForKey:@"aroundShop"];
        weakSelf.mainCell.recentVisitor = [responseObject objectForKey:@"recentVisitor"];
        
        [weakSelf.mainTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
        CSAlert(@"网络请求失败");
    }];
    
    
    
    
}


#pragma mark  benTopViewDelegate
-(void)changeType:(int)i view:(UIView *)betopView{

    switch (i) {
        case 100:
        {
            self.tableViewType = UITableViewTypeMain;
            [self.mainTableView reloadData];
            
//            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:1];
//            [self.mainTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath, nil] withRowAnimation:UITableViewRowAnimationNone];
            
            
        }
            break;
        case 101:
        {
            self.tableViewType = UITableViewTypeActvity;
            [self.mainTableView reloadData];

//            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:1];
//            [self.mainTableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath, nil] withRowAnimation:UITableViewRowAnimationNone];


        }
            break;
        case 102:
        {
            self.tableViewType = UITableViewTypeSchedule;
            [self.mainTableView reloadData];

        }
            break;
        case 103:
        {
            self.tableViewType = UITableViewTypeHonorWall;
            [self.mainTableView reloadData];

        }
            break;
        case 104:
        {
            self.tableViewType = uitableViewTypeLanguagePresidents;
            [self.mainTableView reloadData];

        }
            break;
            
        default:
            break;
    }
    
//    [self.mainTableView reloadData];
}


#pragma mark 点击分享按扭
-(void)clickShare{
    NSLog(@"点击分享按扭");
    
    
}

#pragma mark 点击对话按扭
-(void)clickMessage{
    NSLog(@"点击对话按扭");

    
}



-(void)clicktelShop:(NSString *)telNum{
   [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",telNum]]];
}
-(void)findSchduleDetail:(NSDictionary *)schDic{
    
    
    BMSQ_CourseContentViewController *vc = [[BMSQ_CourseContentViewController alloc]init];
    NSDictionary *dic = schDic;
    vc.classCode = [dic objectForKey:@"classCode"];
    vc.beforeClassInfo = dic;
    vc.shopCode = [self.shopInfo objectForKey:@"shopCode"];
    vc.className = [dic objectForKey:@"className"];
    [self.navigationController pushViewController:vc animated:YES];


}

#pragma mark 教师赞
-(void)clickTeacher:(NSNotification *)noti{

    NSString *teacherCode = noti.object;
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:teacherCode forKey:@"teacherCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"clickTeacher" strParams:teacherCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"clickTeacher" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"赞");
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
      
    }];
    
    

}
#pragma mark 教师取消赞
-(void)cancelClickTeacher:(NSNotification *)noti{
    NSString *teacherCode = noti.object;
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:teacherCode forKey:@"teacherCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"cancelClickTeacher" strParams:teacherCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"cancelClickTeacher" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"取消赞");
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    
    }];
}
-(void)gotoTeaDetailVC:(NSNotification *)noti{
    int i = [[NSString stringWithFormat:@"%@",noti.object]intValue];
    
    BMSQ_TeaDetailViewController *vc = [[BMSQ_TeaDetailViewController alloc]init];
    NSDictionary *dic = [self.shopTeacher objectAtIndex:i];
    vc.teacherCode = [dic objectForKey:@"teacherCode"];
    vc.teaTitel = [dic objectForKey:@"teacherTitle"];
    vc.isTeacher = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
    
}

#pragma mark 领取优惠券
-(void)receivceCoupon:(NSDictionary *)dic{
    NSLog(@"-->%@",dic);
    
    //    NSLog(@"抢");
    
    
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dic objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[dic objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券成功"];
            
            [weakSelf getShopUserCoupon];
            
            
        }
        else if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券失败"];
        }
        else if ([code isEqualToString:@"20000"])
        {
            [self showAlertView:@"失败，请重试"];
        }
        else if ([code isEqualToString:@"80221"])
        {
            [self showAlertView:@"抢完了"];
        }
        else if ([code isEqualToString:@"80220"])
        {
            [self showAlertView:@"过期了"];
        }
        else if ([code isEqualToString:@"80220"])
        {
            [self showAlertView:@"过期了"];
        }
        else if ([code isEqualToString:@"80222"])
        {
            [self showAlertView:@"领用/抢的数量达到上限"];
        }
        else if ([code isEqualToString:@"80223"])
        {
            [self showAlertView:@"优惠券不存在，不可领"];
        }
        else if ([code isEqualToString:@"80235"])
        {
            [self showAlertView:@"你已享受过该特权"];
        }
        
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"freshEff" object:nil];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self showAlertView:@"抢券失败"];
    }];

}

-(void)getShopUserCoupon{
    [self initJsonPrcClient:@"2"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[self.shopInfo objectForKey:@"shopCode"] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopUserCoupon" strParams:[self.shopInfo objectForKey:@"shopCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopUserCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        weakSelf.shopCoupon =  [responseObject objectForKey:@"shopCoupon"];
        weakSelf.userCoupon = [responseObject objectForKey:@"userCoupon"];
        NSIndexSet *indexSet=[[NSIndexSet alloc]initWithIndex:2];
        [weakSelf.mainTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
        
        indexSet=[[NSIndexSet alloc]initWithIndex:3];
        [weakSelf.mainTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
       
        [SVProgressHUD dismiss];

        
        
    }];
}
#pragma mark 使用优惠券
-(void)useCoupon:(NSDictionary *)dic{
    
    int type =(int)[[dic objectForKey:@"couponType"]integerValue];
    
    switch (type) {
        case 1:  //N元购
        {
            
            [self payCard:dic];
        }
            break;
        case 2:
        {
        }
            break;
        case 3:   //抵扣券 折扣券 一样
        {
            [self buyClick:dic];
            
        }
            break;
        case 4:   //折扣券 抵扣券 一样
        {
            [self buyClick:dic];
        }
            break;
        case 5:    //实物券 体验券 一样
        {
            
            [self gotoPay_secVC:dic];
            
            
        }
            break;
        case 6:
        {
            
            [self gotoPay_secVC:dic];
            
            
        }
            break;
        case 32:
        {
            
            [self buyClick:dic];
            
            
        }
            break;
        case 33:
        {
            
            [self buyClick:dic];
            
            
        }
            break;
        case 7:
        {
            
            BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
            couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
            [self.navigationController pushViewController:couponVC animated:YES];
            
            
        }
            break;
        case 8:
        {
            BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
            couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
            [self.navigationController pushViewController:couponVC animated:YES];
            
        }
            break;
        default:
            break;
    }

}

// N元购
-(void)payCard:(NSDictionary *)dic{
    
    BMSQ_PayDetailViewController *vc = [[BMSQ_PayDetailViewController alloc]init];
    vc.shopCode =[dic objectForKey:@"shopCode"];
    vc.shopName = [dic objectForKey:@"shopName"];
    if ([dic objectForKey:@"userCouponCode"]) {
        vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
        
    }else{
        vc.userCouponCode = @"";
        
    }
    vc.formVc = (int)self.navigationController.viewControllers.count;
    [self.navigationController pushViewController:vc animated:YES];
    
}

// 实物 体验券使用
-(void)gotoPay_secVC:(NSDictionary *)dic{
    
    Pay_SecViewController *vc = [[Pay_SecViewController alloc]init];
    vc.myTitle =[dic objectForKey:@"shopName"];
    vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
    vc.shopCode = [dic objectForKey:@"shopCode"];
//    vc.imageUrl =self.shopImage;
    [self.navigationController pushViewController:vc animated:YES];
}

//-- 使用工行卡支付  --
-(void)buyClick:(NSDictionary *)dic{
    
    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
    vc.shopCode = [dic objectForKey:@"shopCode"];
    vc.shopName = [dic objectForKey:@"shopName"];
    if ([dic objectForKey:@"batchCouponCode"]) {
        vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    }else{
        vc.batchCouponCode = @"";
    }
    vc.type = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"couponType"]] intValue];
    vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    vc.fromVC = (int)self.navigationController.viewControllers.count;
    vc.isNeedDiscount = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}
#pragma mark 分享优惠券
-(void)shareCoupon:(NSDictionary *)dic{
    NSLog(@"分享优惠券-->");

}
#pragma mark 登录
-(void)userlogin{
    [self getLogin];
}


#pragma mark 优惠券、活动 展开 隐藏
-(void)clickShowButton:(UIButton *)button{
    button.selected = !button.selected;
    
    if (button.tag == 2000) {  //我的优惠券
        self.isSHowUserCoupon = button.selected;
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:2];
        [self.mainTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
    }else if (button.tag == 2001){ //商家优惠券
        
        self.isSHowShopCoupon = button.selected;
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:3];
        [self.mainTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    }else if(button.tag == 2002){ //商家活动
        self.isSHowAct = button.selected;
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:4];
        [self.mainTableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
    }
    
    
    
}


-(void)scrollViewDidScroll:(UIScrollView *)scrollView{
    UIColor *color = APP_NAVCOLOR;
    CGFloat offset=scrollView.contentOffset.y;
    if (offset<=0) {
        UIImageView *navImage = [self.navigationView viewWithTag:999];
        navImage.hidden = NO;
        [self setNavBackGroundColor:[color colorWithAlphaComponent:0]];
    }else {
        CGFloat alpha=1-((200-offset)/200);
        UIImageView *navImage = [self.navigationView viewWithTag:999];
        navImage.hidden = YES;
        [self setNavBackGroundColor:[color colorWithAlphaComponent:alpha]];
        
        if (alpha>1) {
            self.mainTableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
        }else{
            self.mainTableView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT);
        }
    }
}


@end
