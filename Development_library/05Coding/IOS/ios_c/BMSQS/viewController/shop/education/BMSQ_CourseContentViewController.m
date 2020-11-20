//
//  BMSQ_CourseContentViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_CourseContentViewController.h"

#import "BMSQ_RecStuViewController.h"
#import "BMSQ_CommitViewController.h"
#import "CommitCell.h"
#import "EducationCell.h"
#import "ScheduleStr.h"
#import "MobClick.h"
@interface BMSQ_CourseContentViewController ()<UITableViewDataSource,UITableViewDelegate>


@property (nonatomic, strong)NSArray *classInfoS;
@property (nonatomic, strong)NSDictionary *classInfo;
@property (nonatomic,strong)UITableView *classtableView;

@property (nonatomic, strong)NSMutableArray *ClassRemarkList;

@property (nonatomic, strong)UILabel *zanLabel;
@property (nonatomic, strong)UIButton *zanBtn;


@property(nonatomic, strong)NSString *classTime;
@property(nonatomic, assign)float classHeight;

@end

@implementation BMSQ_CourseContentViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CourseContent"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CourseContent"];
}

-(void)viewDidLoad{
    
    
    [super viewDidLoad];
    
    [self setNavTitle:self.className];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavBackGroundColor:[UIColor clearColor]];
    UIImageView *navImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y)];
    [navImage setImage:[UIImage imageNamed:@"app_nav"]];
    navImage.tag = 999;
    [self.navigationView addSubview:navImage];
    [self.navigationView  sendSubviewToBack:navImage];
    
    NSDictionary *classTimeDic = [ScheduleStr scheduleStr:self.beforeClassInfo w:APP_VIEW_WIDTH-90];
    self.classTime = [classTimeDic objectForKey:@"str"];
    self.classHeight = [[classTimeDic objectForKey:@"height"]floatValue];
    
    self.classInfoS = @[@"所开班级",@"学习时间",@"上课时间",@"适合年龄段",@"报名费用",@"所学课时",@"任课老师",@"报名时间",@"课程简介"];
    self.ClassRemarkList = [[NSMutableArray alloc]init];
    UIButton *btn1 = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 20,  APP_VIEW_ORIGIN_Y-20, APP_VIEW_ORIGIN_Y-20)];
    [btn1 setTitle:@"分享" forState:UIControlStateNormal];
    [btn1 setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    btn1.titleLabel.font = [UIFont boldSystemFontOfSize:14];
    //    [btn1 addTarget:self action:@selector(clickMessage) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:btn1];
    
     self.classtableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-50)];
    self.classtableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.classtableView.delegate = self;
    self.classtableView.dataSource = self;
    self.classtableView.backgroundColor = [UIColor clearColor];
    [self.view insertSubview:self.classtableView belowSubview:self.navigationView];

    
 
    
    UIView *bottomView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-50, APP_VIEW_WIDTH,50)];
    bottomView.backgroundColor = UICOLOR(235, 235, 235, 1);
    [self.view addSubview:bottomView];
    
    
    
    self.zanLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 25, 50, 25)];
    self.zanLabel.textAlignment = NSTextAlignmentCenter;
    self.zanLabel.backgroundColor = [UIColor clearColor];
    self.zanLabel.text = @"25";
    self.zanLabel.textColor =UICOLOR(117, 117, 117, 1);
    self.zanLabel.font = [UIFont systemFontOfSize:12.f];
    [bottomView addSubview:self.zanLabel];
    
    
    
    self.zanBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 50, 50)];
    self.zanBtn.backgroundColor = [UIColor clearColor];
    self.zanBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    [self.zanBtn setImage:[UIImage imageNamed:@"icon_zan_sele"] forState:UIControlStateSelected];
    [self.zanBtn setImage:[UIImage imageNamed:@"icon_zan_gray"] forState:UIControlStateNormal];
    [self.zanBtn addTarget:self action:@selector(clickZan:) forControlEvents:UIControlEventTouchUpInside];

    self.zanBtn.imageEdgeInsets = UIEdgeInsetsMake(-20, 0, 0, 0);
    [bottomView addSubview:self.zanBtn];
    
    UIButton *bottomBtn = [[UIButton alloc]initWithFrame:CGRectMake(50, 0, APP_VIEW_WIDTH-100, 50)];
    [bottomBtn setTitle:@"报名" forState:UIControlStateNormal];
    [bottomBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    bottomBtn.backgroundColor = APP_NAVCOLOR;
    bottomBtn.titleLabel.font = [UIFont boldSystemFontOfSize:18.f];
    [bottomView addSubview:bottomBtn];
    [bottomBtn addTarget:self action:@selector(clickRecButton) forControlEvents:UIControlEventTouchUpInside];
    

    UIButton *commintBtn = [[UIButton alloc]initWithFrame:CGRectMake(50+bottomBtn.frame.size.width, 0, 50, 50)];
    commintBtn.backgroundColor = [UIColor clearColor];
    commintBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    [commintBtn setImage:[UIImage imageNamed:@"iconfont-xiedianping"] forState:UIControlStateNormal];
    [commintBtn setTitleColor:UICOLOR(117, 117, 117, 1) forState:UIControlStateNormal];
    [commintBtn setTitle:@"写点评" forState:UIControlStateNormal];
    commintBtn.titleLabel.font = [UIFont systemFontOfSize:12];
    commintBtn.imageEdgeInsets =  UIEdgeInsetsMake(-20, 10, 0, 0);
    commintBtn.titleEdgeInsets = UIEdgeInsetsMake(35,-20, 10, 0);
    [bottomView addSubview:commintBtn];
    [commintBtn addTarget:self action:@selector(clickCommintButton) forControlEvents:UIControlEventTouchUpInside];
    [self getClassInfo];
    [self getClassRemarkList];
    
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section==0) {
        return 2;
    }else if (section==1){
        return self.classInfoS.count;
    }else if (section==2){
        return self.ClassRemarkList.count;
    }
    
    return 10;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section==0) {
        if (indexPath.row ==0) {
            return APP_VIEW_HEIGHT/3+10;
        }else{
            return 35;
        }
    }else if (indexPath.section ==1){
        
        if (indexPath.row ==1) {  //学习时间
            return self.classHeight+11;
        }
        return 41;
        
//        return 100;
    }else if (indexPath.section ==2){
        
        if (self.ClassRemarkList.count>0) {
            NSDictionary *dic = [self.ClassRemarkList objectAtIndex:indexPath.row];
            return [CommitCell commitHeigh:dic];

        }else{
            return 0;
        }
    }
    
    return 0;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (indexPath.section ==0) {
        if (indexPath.row ==0) {
            
            static NSString *identifier = @"courceSectionCell";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell==nil) {
                
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor clearColor];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
                
                UIImageView *imageV = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT/3)];
                imageV.backgroundColor = [UIColor whiteColor];
                [imageV setImage:[UIImage imageNamed:@"iv_detailNodata"]];
                imageV.tag = 100;
                [cell addSubview:imageV];
                
                
                
                UIView *bottomView = [[UIView alloc]initWithFrame:CGRectMake(0,imageV.frame.size.height-25, APP_VIEW_WIDTH, 25)];
                bottomView.backgroundColor = UICOLOR(153, 153, 153, 1) ;
                [imageV addSubview:bottomView];
                
                UILabel *nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-110, 25)];
                nameLabel.textColor = [UIColor whiteColor];
                nameLabel.font = [UIFont systemFontOfSize:11];
                nameLabel.tag = 200;
                [bottomView addSubview:nameLabel];
                
                UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 90, 25)];
                countLabel.textColor = [UIColor whiteColor];
                countLabel.font = [UIFont systemFontOfSize:11];
                countLabel.textAlignment = NSTextAlignmentRight;
                countLabel.tag = 300;
                [bottomView addSubview:countLabel];
                
            }
            UIImageView *bgImageView = (UIImageView *)[cell viewWithTag:100];
            UILabel *namelabel =(UILabel *)[cell viewWithTag:200];
            UILabel *countLabel =(UILabel *)[cell viewWithTag:300];
           [ bgImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.classInfo objectForKey:@"classUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
            
            if ([self.classInfo objectForKey:@"className"]) {
                namelabel.text = [self.classInfo objectForKey:@"className"];
                
            }
            
            if ([self.classInfo objectForKey:@"studentNum"]) {
                countLabel.text = [NSString stringWithFormat:@"人数:%@人",[self.classInfo objectForKey:@"studentNum"]];
                
            }
            return cell;

        }else{
            static NSString *identifier = @"courceSectionCell1";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
            if (cell==nil) {
                
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
                cell.backgroundColor = [UIColor whiteColor];
                cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
                cell.textLabel.font = [UIFont systemFontOfSize:13];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;

                cell.textLabel.textColor = APP_TEXTCOLOR;
                
                UIImageView *imageIcon = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 12, 15)];
                [imageIcon setImage:[UIImage imageNamed:@"iv_phone2"]];
                [cell addSubview:imageIcon];
                
                
                UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 75, 35)];
                label.backgroundColor = [UIColor clearColor];
                label.text = @"免费咨询";
                label.textColor = APP_TEXTCOLOR;
                label.textAlignment = NSTextAlignmentRight;
                label.font = [UIFont systemFontOfSize:12];
                [cell addSubview:label];
                
                
            }
            if ([self.classInfo objectForKey:@"hotline"]) {
                cell.textLabel.text = [NSString stringWithFormat:@"       %@",[self.classInfo objectForKey:@"hotline"] ];

            }
            
            return cell;
        }
        
        
    }
    else if (indexPath.section ==1){

        return [EducationCell setClassInfo:tableView class:self.classInfo left:self.classInfoS indexPath:indexPath classTime:self.classTime classHeigh:self.classHeight+11];
    }
    else if (indexPath.section ==2){
        
        static NSString *identifier = @"commitCell";
        CommitCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell==nil) {
            cell = [[CommitCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.backgroundColor = [UIColor whiteColor];
        }
        NSDictionary *dic = [self.ClassRemarkList objectAtIndex:indexPath.row];
        [cell setCommitCell:dic];
        
        return cell;

        
    }
    static NSString *identifier = @"";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell==nil) {
        
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.backgroundColor = [UIColor whiteColor];
    }
    return cell;
    
    
}

-(UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
    bgView.backgroundColor = APP_VIEW_BACKCOLOR;
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
    [bgView addSubview:label];
    label.textColor = APP_TEXTCOLOR;
    label.font = [UIFont systemFontOfSize:13];
    if (section==1) {
        label.text = @"    课程介绍";
    }else if(section ==2){
        label.text = @"    网友点评";
    }
    
    return bgView;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    if (section==1) {
        return 20;
    }else if(section ==2){
        if (self.ClassRemarkList.count>0) {
            return 20;
        }else{
            return 0;
        }
        
    }else{
        return 0;
    }
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if(indexPath.section ==0){
        if (indexPath.row ==1) {
            
            if ([self.classInfo objectForKey:@"hotline"]) {
                [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",[self.classInfo objectForKey:@"hotline"]]]];
            }
        }
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
    }
}
#pragma mark 报名
-(void)clickRecButton{
    
    
    if(![gloabFunction isLogin]){
        [self getLogin];
    }else{
        BMSQ_RecStuViewController *vc = [[BMSQ_RecStuViewController alloc]init];
        vc.classDic = self.classInfo;
        vc.classTimeDic = self.beforeClassInfo;
        vc.shopCode = self.shopCode;
        [self.navigationController pushViewController:vc animated:YES];

    }
}
#pragma mark 评论
-(void)clickCommintButton{
    
    if(![gloabFunction isLogin]){
        [self getLogin];
    }else{
        BMSQ_CommitViewController *vc = [[BMSQ_CommitViewController alloc]init];
        vc.classCode = [self.classInfo objectForKey:@"classCode"];
        [self.navigationController pushViewController:vc animated:YES];
    }
    
   

}

#pragma mark ----- getShopInfo  课程详情
- (void)getClassInfo
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getClassInfo" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getClassInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        weakSelf.classInfo = responseObject;
        
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:0];
        [weakSelf.classtableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
        indexSet = [[NSIndexSet alloc]initWithIndex:1];
        [weakSelf.classtableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
        NSString *clickNbr = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"clickNbr"]];
        weakSelf.zanLabel.text = clickNbr;
        
        NSString *isUserClick = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"isUserClick"]];
        weakSelf.zanBtn.selected = [isUserClick intValue]==1?YES:NO;
        
        if(weakSelf.zanBtn.selected){
            weakSelf.zanLabel.textColor = APP_NAVCOLOR;
        }else{
            weakSelf.zanLabel.textColor = UICOLOR(117, 117, 117, 1);
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
    
    
}
#pragma mark ----- getClassRemarkList  
- (void)getClassRemarkList
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:@"0" forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getClassRemarkList" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getClassRemarkList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSArray *array = [responseObject objectForKey:@"list"];
        [weakSelf.ClassRemarkList addObjectsFromArray:array];
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:2];
        [weakSelf.classtableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    
    
    
}
#pragma mark 赞
-(void)clickZan:(UIButton *)button{
    
    if (![gloabFunction isLogin]) {
        [self getLogin];
    }
    else
    {
        button.selected = !button.selected;
        if (button.selected) {
            self.zanLabel.textColor = APP_NAVCOLOR;
        }else{
            self.zanLabel.textColor = UICOLOR(117, 117, 117, 1);
        }
        CAKeyframeAnimation *k = [CAKeyframeAnimation animationWithKeyPath:@"transform.scale"];
        k.values = @[@(0.1),@(1.0),@(1.5)];
        k.keyTimes = @[@(0.0),@(0.5),@(0.8),@(1.0)];
        k.calculationMode = kCAAnimationLinear;
        [button.layer addAnimation:k forKey:@"SHOW"];
        
        NSString *str = self.zanLabel.text;
        if (button.selected) {  //点赞
            [self praiseClass];

            self.zanLabel.text = [NSString stringWithFormat:@"%d",[str intValue]+1];
        }else{   //取消赞
            [self cancelPraiseClass];
            self.zanLabel.text = [NSString stringWithFormat:@"%d",[str intValue]-1];

        }
        
    }
}
-(void)praiseClass{
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];

    NSString* vcode = [gloabFunction getSign:@"praiseClass" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"praiseClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"点赞-->%@",responseObject);
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
    
}
-(void)cancelPraiseClass{
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"cancelPraiseClass" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"cancelPraiseClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"取消-->%@",responseObject);

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
}
@end
