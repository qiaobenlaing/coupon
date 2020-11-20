//
//  BMSQ_CourseViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/27.
//  Copyright © 2016年 djx. All rights reserved.
//




#define HIGHT 40            //所有的高
#define WIDTH 90


#import "BMSQ_CourseViewController.h"
#import "BMSQ_addCourseViewController.h"
#import "BMSQ_courseDetailViewController.h"

@interface BMSQ_CourseViewController ()<UITableViewDataSource,UITableViewDelegate,UIScrollViewDelegate>

{
    NSArray *tableArray;
    float scorllContentWidth;
}
@property (nonatomic,strong)UITableView *leftTableView;
@property (nonatomic,strong)UIScrollView *rightScrollView;
@property (nonatomic,strong)UIScrollView *topScrollView;

@property (nonatomic,strong)NSArray *courseArray;


@end

@implementation BMSQ_CourseViewController

- (void)viewDidLoad {
    [super viewDidLoad];


    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"课程表"];

    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(loadCourse) name:@"loadCourse" object:nil];
    
    UIButton *addBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 20, 50, 40)];
    addBtn.backgroundColor = [UIColor clearColor];
    [addBtn setTitle:@"添加" forState:UIControlStateNormal];
    [addBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    addBtn.titleLabel.font = [UIFont boldSystemFontOfSize:13];
    [addBtn addTarget:self action:@selector(addCourse) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:addBtn];
    
    self.view.backgroundColor = [UIColor whiteColor];
    tableArray = @[@"6AM",@"7AM",@"8AM",@"9AM",@"10AM",@"11AM",@"NOON",@"1PM",@"2PM",@"3PM",@"4PM",@"5PM",@"6PM",@"7PM",@"8PM",@"9PM",@"10PM"];
    [self addScrollView];
    [self getUserClassList];
}
-(void)addScrollView{
    
    self.leftTableView = [[UITableView alloc]initWithFrame:CGRectMake(0,APP_VIEW_ORIGIN_Y+40,60, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-50)];
    self.leftTableView.backgroundColor = [UIColor clearColor];
    self.leftTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.leftTableView.delegate = self;
    self.leftTableView.dataSource = self;
    self.leftTableView.rowHeight = HIGHT;
    self.leftTableView.showsVerticalScrollIndicator = NO;
    self.leftTableView.showsHorizontalScrollIndicator = NO;
    [self.view addSubview:self.leftTableView];
    
    //top
    
    UIView *bgTopView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 40)];
    bgTopView.backgroundColor = UICOLOR(235, 235, 235, 1);
    [self.view addSubview:bgTopView];
    
    
    self.topScrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(self.leftTableView.frame.size.width, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH - self.leftTableView.frame.size.width, bgTopView.frame.size.height)];
    self.topScrollView.backgroundColor = [UIColor clearColor];
    self.topScrollView.delegate = self;
    [self.view addSubview:self.topScrollView];
    self.topScrollView.showsVerticalScrollIndicator = NO;
    self.topScrollView.showsHorizontalScrollIndicator = NO;
    
    NSArray *weekS = @[@"周一",@"周二",@"周三",@"周四",@"周五",@"周六",@"周日"];
    for (int i = 0; i<weekS.count; i++) {
        UIButton *weekButon = [[UIButton alloc]initWithFrame:CGRectMake(i*WIDTH, 0, WIDTH, self.topScrollView.frame.size.height)];
        weekButon.backgroundColor = [UIColor clearColor];
        [weekButon setTitle:[weekS objectAtIndex:i] forState:UIControlStateNormal];
        [weekButon setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        weekButon.titleLabel.font = [UIFont systemFontOfSize:12.f];
        weekButon.tag = 100+i;
        [self.topScrollView addSubview:weekButon];
        
    }
    
    self.topScrollView.contentSize = CGSizeMake(WIDTH*weekS.count, bgTopView.frame.size.height);
    scorllContentWidth =WIDTH*weekS.count;
    
    
    
    //右
    self.rightScrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(self.leftTableView.frame.size.width, self.topScrollView.frame.origin.y+self.topScrollView.frame.size.height, self.view.frame.size.width - self.leftTableView.frame.size.width, self.leftTableView.frame.size.height)];
    self.rightScrollView.backgroundColor = [UIColor whiteColor];
    self.rightScrollView.delegate = self;
    self.rightScrollView.showsHorizontalScrollIndicator = NO;
    self.rightScrollView.showsVerticalScrollIndicator = NO;
    [self.view addSubview:self.rightScrollView];
    
    
    self.rightScrollView.contentSize = CGSizeMake(WIDTH*weekS.count, tableArray.count*self.leftTableView.rowHeight);
    
    
    
    //横线
    for (int i = 0;i<tableArray.count;i++) {
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, i*HIGHT-1,  self.rightScrollView.contentSize.width, 1)];
        lineView.backgroundColor = APP_VIEW_BACKCOLOR;
        lineView.tag = 1000+i;
        [self.rightScrollView addSubview:lineView];
    }
    
    //竖线
    for (int i = 0; i < weekS.count; i++) {
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(WIDTH*(i+1), 0, 1, self.rightScrollView.contentSize.height)];
        lineView.backgroundColor = APP_VIEW_BACKCOLOR;
        lineView.tag = 2000+i;
        [self.rightScrollView addSubview:lineView];
    }
    
    
    /*
     UIView *lineView = [self.rightScrollView viewWithTag:1016];
     CGRect rect = lineView.frame;
     
     for (int i=0; i<weekS.count; i++) {
     UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake((10)+i*WIDTH,rect.origin.y+5, WIDTH-20, HIGHT-10)];
     label.backgroundColor =UICOLOR(236, 41, 51, 1);
     label.text = @"美术教室";
     label.textColor = [UIColor whiteColor ];
     label.font = [UIFont boldSystemFontOfSize:12.f];
     label.numberOfLines = 2;
     label.layer.cornerRadius = 4;
     label.layer.masksToBounds = YES;
     
     label.textAlignment = NSTextAlignmentCenter;
     [self.rightScrollView addSubview:label];
     }
     
     UIView *lineView = [self.rightScrollView viewWithTag:2000];
     CGRect rect = lineView.frame;
     
     for (int i=0; i<tableArray.count; i++) {
     UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake((rect.origin.x-WIDTH)+5, i*HIGHT+5, WIDTH-10, HIGHT-10)];
     label.backgroundColor =UICOLOR(236, 41, 51, 1);
     label.text = @"美术教室";
     label.textColor = [UIColor whiteColor ];
     label.font = [UIFont boldSystemFontOfSize:12.f];
     label.numberOfLines = 2;
     label.layer.cornerRadius = 4;
     label.layer.masksToBounds = YES;
     
     label.textAlignment = NSTextAlignmentCenter;
     [self.rightScrollView addSubview:label];
     }
     */
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return tableArray.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifer = @"identifer";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifer];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifer];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        UILabel *leftLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, HIGHT-20, self.leftTableView.frame.size.width, 20)];
        leftLabel.textColor = APP_TEXTCOLOR;
        leftLabel.backgroundColor = [UIColor clearColor];
        leftLabel.font = [UIFont systemFontOfSize:14];
        leftLabel.tag = 100;
        leftLabel.textAlignment = NSTextAlignmentCenter;
        [cell addSubview:leftLabel];
        
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, HIGHT-1, self.leftTableView.frame.size.width, 1)];
        lineView.backgroundColor = APP_VIEW_BACKCOLOR;
        lineView.hidden = YES;
        lineView.tag = 200;
        [cell addSubview:lineView];
        
        UIView *rightView = [[UIView alloc]initWithFrame:CGRectMake(self.leftTableView.frame.size.width-1, 0, 1,HIGHT)];
        rightView.backgroundColor =APP_VIEW_BACKCOLOR;
        [cell addSubview:rightView];

    }
    
    UILabel *leftLabel = [cell viewWithTag:100];
    UIView *lineView = [cell viewWithTag:200];
    leftLabel.text =[tableArray objectAtIndex:indexPath.row];
    
    if (indexPath.row == tableArray.count-1) {
        lineView.hidden = YES;
    }else {
        lineView.hidden = NO;

    }
    return cell;
}
#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    
    if (scrollView == self.leftTableView) {
        self.rightScrollView.contentOffset = CGPointMake(self.rightScrollView.contentOffset.x, self.leftTableView.contentOffset.y);
    }
    
    if (scrollView == self.topScrollView) {
        self.rightScrollView.contentOffset = CGPointMake(self.topScrollView.contentOffset.x, self.rightScrollView.contentOffset.y);
    }
    
    if (scrollView == self.rightScrollView)
    {
        self.topScrollView.contentOffset = CGPointMake(self.rightScrollView.contentOffset.x, 0);
        self.leftTableView.contentOffset = CGPointMake(0, self.rightScrollView.contentOffset.y);
        
        if (scrollView.contentOffset.x <0 ||scrollView.contentOffset.x > scorllContentWidth - self.rightScrollView.frame.size.width)
        {
            CGPoint point = scrollView.contentOffset;
            point.x = scrollView.contentOffset.x <0? 0:scorllContentWidth - self.rightScrollView.frame.size.width;
            [self.rightScrollView setContentOffset:point];
        }
        
        if (scrollView.contentOffset.y<0 ||scrollView.contentOffset.y> self.rightScrollView.contentSize.height - self.rightScrollView.frame.size.height) {
            CGPoint point = scrollView.contentOffset;
            point.y = scrollView.contentOffset.y<0?0:self.rightScrollView.contentSize.height - self.rightScrollView.frame.size.height;
            [self.rightScrollView setContentOffset:point];
        }
    }
}
#pragma mark - 获取课程
-(void)getUserClassList{
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    NSString* vcode = [gloabFunction getSign:@"getUserClassList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    
    [self.jsonPrcClient invokeMethod:@"getUserClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        weakSelf.courseArray = responseObject;
        [weakSelf addUserCourse];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
}

#pragma mark - 添加用户课程
-(void)addUserCourse{
    
    UIView *YlineView;  //竖线
    CGRect Yrect;
    
    UIView *XlineView;  //竖线
    CGRect Xrect;
    for (int i=0;i<self.courseArray.count;i++) {
        
        NSDictionary *courseDic = [self.courseArray objectAtIndex:i];
//        NSLog(@"courseAddr = %@",[courseDic objectForKey:@"courseAddr"]);
//        NSLog(@"courseName = %@",[courseDic objectForKey:@"courseName"]);
//        NSLog(@"end = %@",[courseDic objectForKey:@"end"]);
//        NSLog(@"start = %@",[courseDic objectForKey:@"start"]);
//        NSLog(@"tableCode = %@",[courseDic objectForKey:@"tableCode"]);
//        NSLog(@"weekDay = %@",[courseDic objectForKey:@"weekDay"]);
//        NSLog(@"weekDayNum = %@",[courseDic objectForKey:@"weekDayNum"]);
//        NSLog(@"statTime = %d",statTime);
//        NSLog(@"\n\n\n");
        
        NSString *start = [courseDic objectForKey:@"start"];
        NSArray *startArray = [start componentsSeparatedByString:@":"];
        int statTime = [[startArray objectAtIndex:0]intValue];
        
        int weekDayNum= [[NSString stringWithFormat:@"%@",[courseDic objectForKey:@"weekDayNum"]]intValue];  //星期
        
        int tag = weekDayNum-2+2000;
        YlineView = [self.rightScrollView viewWithTag:weekDayNum-2+2000 == 1999?2000:weekDayNum-2+2000];  //竖线
        Yrect = YlineView.frame;
        
        XlineView = [self.rightScrollView viewWithTag:statTime+5-10+1000];
        Xrect = XlineView.frame;
        float x ;
        if (tag == 1999) {
            x = (Yrect.origin.x-WIDTH)+5;
        }else{
            x = Yrect.origin.x+5;
        }
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(x,Xrect.origin.y+5, WIDTH-10, HIGHT-10)];
        label.backgroundColor =UICOLOR(236, 41, 51, 1);
        label.text = [courseDic objectForKey:@"courseName"];
        label.textColor = [UIColor whiteColor ];
        label.font = [UIFont boldSystemFontOfSize:12.f];
        label.numberOfLines = 2;
        label.layer.cornerRadius = 4;
        label.layer.masksToBounds = YES;
        label.textAlignment = NSTextAlignmentCenter;
        label.tag = i;
        [self.rightScrollView addSubview:label];
        
        label.userInteractionEnabled = YES;
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(findDetail:)];
        [label addGestureRecognizer:tapGesture];
        
        
    }
    

}
-(void)findDetail:(UITapGestureRecognizer *)tapGesture{
    
    
    UILabel *classLabe = (UILabel *)tapGesture.view;
     int tag = (int)classLabe.tag;
    NSDictionary *classCourse = [self.courseArray objectAtIndex:tag];
    
    BMSQ_courseDetailViewController *vc = [[BMSQ_courseDetailViewController alloc]init];
    vc.tableCode = [classCourse objectForKey:@"tableCode"];
    [self.navigationController pushViewController:vc animated:YES];

}

#pragma mark - 添加课程
-(void)addCourse{
    BMSQ_addCourseViewController *vc = [[BMSQ_addCourseViewController alloc]init];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - 刷新课程
-(void)loadCourse{
    for (UIView *subView in self.rightScrollView.subviews) {
        if ([subView isKindOfClass:[UILabel class]]) {
            [subView removeFromSuperview];
        }
    }
    [self getUserClassList];
    
}


@end
