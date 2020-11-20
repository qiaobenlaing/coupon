//
//  BMSQ_AddStudentViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_AddStudentViewController.h"
#import "BMSQ_AddPersonImageViewController.h"
#import "SVProgressHUD.h"
@interface BMSQ_AddStudentViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong)UIView * backView;//背景视图
@property (nonatomic, strong)UILabel * studentNameLB;// 学员姓名

@property (nonatomic, strong)UIButton * studentListBut;// 学员列表
@property (nonatomic, strong)UIImageView * arrowImage; //箭头
@property (nonatomic, strong)UILabel * studentAge;// 学员年龄
@property (nonatomic, strong)UITextField * studentAgeFeld;
@property (nonatomic, strong)UILabel     * learnSubject;//所学课程
@property (nonatomic, strong)UITextField * learnSubjectFeld;
@property (nonatomic, strong)UILabel     * appellation;//活动称谓
@property (nonatomic, strong)UITextField * appellationFeld;
@property (nonatomic, strong)UILabel     * line1;
@property (nonatomic, strong)UILabel     * line2;
@property (nonatomic, strong)UILabel     * line3;
@property (nonatomic, strong)NSString * stuName;

@property (nonatomic, strong)UITableView * baseView;//班级列表
@property (nonatomic, strong)UITableView * studentView;//学员列表
@property (nonatomic, strong)NSString * classCode;
@property (nonatomic, strong)NSMutableArray * dataSource;
@property (nonatomic, strong)NSMutableArray * studentList;

@property (nonatomic, assign)BOOL isSelect;

@end

@implementation BMSQ_AddStudentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavTitle:@"学员之星"];
    [self setNavBackItem];
    [self customRightBtn];
    self.isSelect = NO;
    self.stuName = self.studentName;
    [self setViewUp];
    [self topBaseView];
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 50, 20, 48, 44);
    [item setTitle:@"下一步" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemssNextClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
}

#pragma mark ------ 
- (void)setViewUp
{
//    self.backView = [[UIView alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH - 20, 143)];
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, 70)];
    _backView.backgroundColor = [UIColor whiteColor];
//    _backView.layer.borderColor = [UIColor grayColor].CGColor;
//    _backView.layer.borderWidth =1.0;
    //    _backView.layer.cornerRadius =5.0;
    [self.view addSubview:self.backView];
    
    self.studentNameLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 80, 20)];
    self.studentNameLB.text = @"学员姓名";
    self.studentNameLB.textColor = UICOLOR(102, 102, 102, 1.0);
    self.studentNameLB.font = [UIFont systemFontOfSize:14.0];
    [self.backView addSubview:self.studentNameLB];
    
    /*
     studentListBut
     arrowTopImage
     arrowBottomImage
     */
    self.studentListBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.studentListBut.frame = CGRectMake(self.backView.frame.size.width - 200, 5, 85, 25);
    self.studentListBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [self.studentListBut setTitle:@"请选择学员" forState:UIControlStateNormal];
    [self.studentListBut setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    self.studentListBut.titleLabel.textAlignment = NSTextAlignmentLeft;
    [self.studentListBut addTarget:self action:@selector(studentListButClick:) forControlEvents:UIControlEventTouchUpInside];
    if (self.studentName.length != 0) {
        [self.studentListBut setTitle:self.studentName forState:UIControlStateNormal];
    }
    [self.backView addSubview:self.studentListBut];
    
    self.arrowImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.studentListBut.frame.origin.x + self.studentListBut.frame.size.width, 12, 8, 8)];
    self.arrowImage.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
//    [self.backView addSubview:self.arrowImage];
    
    
    self.line1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 35, self.backView.frame.size.width, 0.5)];
    self.line1.backgroundColor = APP_CELL_LINE_COLOR;
    [self.backView addSubview:self.line1]; // 竖线1
    

    self.appellation = [[UILabel alloc] initWithFrame:CGRectMake(10, 41, 80, 20)];
    self.appellation.text = @"活动称谓";
    self.appellation.textColor = UICOLOR(102, 102, 102, 1.0);
    self.appellation.font = [UIFont systemFontOfSize:14.0];
    [self.backView addSubview:self.appellation];
    
//    self.appellationFeld = [[UITextField alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 150, 112, 130, 25)];
    self.appellationFeld = [[UITextField alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 200, 41, 180, 25)];
    self.appellationFeld.font = [UIFont systemFontOfSize:14.0];
    self.appellationFeld.placeholder = @"请填写活动称谓";
    if (self.starName.length != 0) {
        self.appellationFeld.text = self.starName;
    }
    [self.backView addSubview:self.appellationFeld];
    
    
}

- (void)topBaseView
{
        self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 200, APP_VIEW_ORIGIN_Y + 35, 100, 180) style:UITableViewStyleGrouped];
        self.baseView.backgroundColor = [UIColor whiteColor];
        self.baseView.rowHeight = 35;
        self.baseView.hidden = YES;
        self.baseView.dataSource = self;
        self.baseView.delegate = self;
        [self.view addSubview:self.baseView];
    
    
        self.studentView = [[UITableView alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 100, APP_VIEW_ORIGIN_Y + 35, 100, 180) style:UITableViewStyleGrouped];
        self.studentView.backgroundColor = [UIColor grayColor];
        self.studentView.rowHeight = 35;
        self.studentView.hidden = YES;
        self.studentView.dataSource = self;
        self.studentView.delegate = self;
        [self.view addSubview:self.studentView];

    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if ([tableView isEqual:self.baseView]) {
        
        if (self.dataSource.count == 0) {
            return 0;
        }else{
            return self.dataSource.count;
        }
        
    }else{
        if (self.studentList.count == 0) {
            return 0;
        }else{
            return self.studentList.count;
        }
    }
    
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"cellsssa";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.textLabel.font = [UIFont systemFontOfSize:12.0];
    }
    if ([tableView isEqual:self.baseView]) {
        cell.textLabel.text = [NSString stringWithFormat:@"%@", self.dataSource[indexPath.row][@"className"]];
    }else if ([tableView isEqual:self.studentView]){
        cell.textLabel.text = [NSString stringWithFormat:@"%@", self.studentList[indexPath.row][@"studentName"]];
    }
    
    return cell;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1.0;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([tableView isEqual:self.baseView]) {
//        NSLog(@"获取班级列表");
        self.classCode = [NSString stringWithFormat:@"%@", self.dataSource[indexPath.row][@"classCode"]];
        
        self.studentView.hidden = NO;
        [self getStudentList];
    }else if ([tableView isEqual:self.studentView]){
//        NSLog(@"获取学员列表");
        self.baseView.hidden = YES;
        self.studentView.hidden = YES;
        self.stuName = [NSString stringWithFormat:@"%@", self.studentList[indexPath.row][@"studentName"]];
        self.signCode = [NSString stringWithFormat:@"%@", self.studentList[indexPath.row][@"signCode"]];
        [self.studentListBut setTitle:self.stuName forState:UIControlStateNormal];
    }
    

    
}

#pragma mark ------  选择姓名
- (void)studentListButClick:(UIButton *)sender
{
    self.isSelect = !self.isSelect;
    if (self.isSelect) {
        self.baseView.hidden = NO;
        self.arrowImage.image = [UIImage imageNamed:@"iv_arrowTopHeight"];
        [self getClassList];
    }else{
        self.baseView.hidden = YES;
        self.studentView.hidden = YES;
        self.arrowImage.image = [UIImage imageNamed:@"iv_arrowBottomHeight"];
    }
    
    
}

#pragma mark -------  getClassList 获取课程列表
- (void)getClassList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getClassList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        self.dataSource = [NSMutableArray arrayWithArray:responseObject];
        int number = (int)self.dataSource.count * 35;
        if (number < 180) {
            self.baseView.frame = CGRectMake(self.backView.frame.size.width - 200, APP_VIEW_ORIGIN_Y + 35, 100, number);
        }
        [self.baseView reloadData];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

    
}

#pragma mark ------ getStudentList 通过课程选择获取学员列表
- (void)getStudentList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:self.classCode forKey:@"classCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getStudentList" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStudentList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        self.studentList = [NSMutableArray arrayWithArray:responseObject];
        if (self.studentList.count == 0) {
            CSAlert(@"该班级没有学员信息");
        }
        int number = (int)self.studentList.count * 35;
        if (number < 180) {
            self.studentView.frame = CGRectMake(self.backView.frame.size.width - 100, APP_VIEW_ORIGIN_Y + 35, 100, number);
        }
        [self.studentView reloadData];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];
}







#pragma mark ------ 下一步
- (void)itemssNextClick:(UIButton *)sender
{
    
    if (self.stuName.length == 0 || self.appellationFeld.text.length == 0) {
        CSAlert(@"请填写完整");
    }else{
        BMSQ_AddPersonImageViewController * addPersonVC = [[BMSQ_AddPersonImageViewController alloc] init];
        addPersonVC.starName = self.appellationFeld.text;
        addPersonVC.starCode = self.starCode;
        addPersonVC.signCode = self.signCode;
        addPersonVC.starInfo = self.starInfo;
        addPersonVC.starUrl = self.starUrl;
        addPersonVC.starWork = self.starWork;
        addPersonVC.number = 2;
        [self.navigationController pushViewController:addPersonVC animated:YES];

    }
}

#pragma mark ------
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
