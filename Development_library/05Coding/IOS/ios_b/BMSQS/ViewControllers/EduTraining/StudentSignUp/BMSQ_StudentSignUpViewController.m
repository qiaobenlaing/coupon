//
//  BMSQ_StudentSignUpViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_StudentSignUpViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "StudentSignUpViewCell.h"
@interface BMSQ_StudentSignUpViewController ()<UITableViewDataSource, UITableViewDelegate, UITextViewDelegate, StudentSignUpViewCellDetegate>

{
    int  handFlag;// 	处理标志
    NSString * handMemo;//  处理意见
    NSString * signCode;//  报名编码
    NSString * signFee;//   报名费用
}

@property (nonatomic, strong)UITableView * tableView;
@property (nonatomic, strong)NSMutableArray * dataSource;
@property (nonatomic, assign)int page;
@property (nonatomic, assign)int  rowNumber;
@property (nonatomic, strong)UIView * bottomView;
@property (nonatomic, strong)UITextView * explainView;
@property (nonatomic, strong)UILabel * textViewPlaceholder;
@property (nonatomic, strong)UIButton * ensureBut;
@property (nonatomic, strong)UIButton * cancelBut;


@end

@implementation BMSQ_StudentSignUpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:@"学员报名"];
    [self setNavBackItem];
    self.dataSource = [@[] mutableCopy];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillShowNotification object:nil];
    
     self.page = 1;
    [self setViewUp];
    [self getStudentClassList];
    
}


- (void)setViewUp
{
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 65) style:UITableViewStyleGrouped];
//    self.tableView.rowHeight = 145;
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.tableView.keyboardDismissMode  = UIScrollViewKeyboardDismissModeInteractive;
    [self.tableView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.tableView addFooterWithTarget:self action:@selector(footerRereshing)];

    [self.view addSubview:self.tableView];
    
    
    self.bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height - 100, APP_VIEW_WIDTH, 100)];
    self.bottomView.hidden = YES;
    self.bottomView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.bottomView];
    [self.view bringSubviewToFront:self.bottomView];
    self.explainView = [[UITextView alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 80, 80)];
    self.explainView.delegate = self;
    self.explainView.layer.borderColor = [UIColor grayColor].CGColor;
    self.explainView.layer.borderWidth =1.0;
    self.explainView.layer.cornerRadius =5.0;
    self.explainView.backgroundColor = [UIColor whiteColor];
    [self.bottomView addSubview:self.explainView];
    
    self.textViewPlaceholder = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 15)];
    self.textViewPlaceholder.font = [UIFont systemFontOfSize:12.f];
    self.textViewPlaceholder.textColor = UICOLOR(205, 205, 209, 1.0);
    [self.explainView addSubview:self.textViewPlaceholder];
    
    self.ensureBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.ensureBut.frame = CGRectMake(APP_VIEW_WIDTH - 65, 20, 60, 30);
    self.ensureBut.backgroundColor = [UIColor grayColor];
    [self.ensureBut addTarget:self action:@selector(ensureBuClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.ensureBut setTitle:@"确定" forState:UIControlStateNormal];
    [self.bottomView addSubview:self.ensureBut];
    
    
    self.cancelBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.cancelBut.frame = CGRectMake(APP_VIEW_WIDTH - 65, 60, 60, 30);
    self.cancelBut.backgroundColor = [UIColor grayColor];
    [self.cancelBut addTarget:self action:@selector(cancelBuClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.cancelBut setTitle:@"取消" forState:UIControlStateNormal];
    [self.bottomView addSubview:self.cancelBut];
    
    
}

#pragma mark ------ UITextViewDelegate
- (void)textViewDidBeginEditing:(UITextView *)textView
{
    self.textViewPlaceholder.hidden = YES;
    self.bottomView.frame = CGRectMake(0, self.view.frame.size.height - 356, APP_VIEW_WIDTH, 100);
}
- (void)textViewDidEndEditing:(UITextView *)textView
{
    [textView resignFirstResponder];
}
#pragma mark ------ UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataSource.count == 0) {
        return 0;
    }else{
        return self.dataSource.count;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"StudentSignUpViewCellsdf";
    StudentSignUpViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[StudentSignUpViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.studentSignUpDetegate = self;
    
    [cell setCellWithSignUpDic:self.dataSource[indexPath.row] row:(int)indexPath.row];
    
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

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * Flag = [NSString stringWithFormat:@"%@", self.dataSource[indexPath.row][@"handFlag"]];
    NSString * str = @"";
    if (Flag.intValue == 2){
        str = [NSString stringWithFormat:@"%@", self.dataSource[indexPath.row][@"handMemo"]];
        CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH -  40, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:12.f]} context:nil].size;
        if (size.height > 34) {
            return 106 + size.height;
        }
        return 140;
        
    }else if (Flag.intValue == 1){
        
        return 140;
    }else{
        return 145;
    }
    
}


#pragma mark ------ 确定
- (void)ensureBuClick:(UIButton *)sender
{
    
    signCode = [NSString stringWithFormat:@"%@", self.dataSource[self.rowNumber][@"signCode"]];
    if (handFlag == 1) {
        handMemo = @"";
        signFee = self.explainView.text;
        if (signFee.length != 0) {
            [self dealStudentClass];
        }else{
            CSAlert(@"请输入价格");
        }
        
    }else if (handFlag == 2){
        handMemo = self.explainView.text;
        signFee = @"0";
        if (handMemo.length != 0) {
            [self dealStudentClass];
        }else{
            CSAlert(@"请输入意见说明");
        }
    }
    
    
   
    
}


#pragma mark ----- 取消
- (void)cancelBuClick:(UIButton *)sender
{
    self.bottomView.hidden = YES;
    self.textViewPlaceholder.hidden = NO;
    self.bottomView.frame = CGRectMake(0, self.view.frame.size.height - 100, APP_VIEW_WIDTH, 100);
    self.explainView.text = @"";
    [self textViewDidEndEditing:self.explainView];
}

#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    
    self.page = 1;
    [self getStudentClassList];
}
- (void)footerRereshing{
    self.page ++;
    [self getStudentClassList];
}


#pragma mark ------ StudentSignUpViewCellDetegate
- (void)setCellStudentSignUp:(int)hand  learnFee:(NSString *)learnFee row:(int)row
{
    if (hand == 1) {
        if (learnFee.intValue == 0) {
            self.textViewPlaceholder.hidden = NO;
            self.textViewPlaceholder.text = @"请填写报名价格";
        }else{
            self.explainView.text = learnFee;
            self.textViewPlaceholder.hidden = YES;
        }
        self.rowNumber = row;
        handFlag = 1;
        self.bottomView.hidden = NO;
    }else if (hand == 2){
        handFlag = 2;
        self.textViewPlaceholder.text = @"请填写拒绝说明";
        NSString * str;
        self.explainView.text = str;
        self.bottomView.hidden = NO;
        self.rowNumber = row;
    }
}

#pragma mark ---------  getStudentClassList 获取学员报名列表
- (void)getStudentClassList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    [params setObject:@"" forKey:@"classCode"];// 班级编码
    [params setObject:@"-1" forKey:@"feeFlag"];// 缴费标志
    [params setObject:@"-1" forKey:@"handFlag"];// 处理标志
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getStudentClassList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStudentClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
        
        if(self.page == 1) {
            [self.dataSource removeAllObjects];
        }
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            [self.dataSource addObjectsFromArray:responseObject[@"studentClassList"]];
        }
        if (self.dataSource.count == 0) {
            CSAlert(@"暂无学员报名信息");
        }
        
//        NSLog(@"%@", self.dataSource);
        [self.tableView reloadData];


        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}


#pragma mark ---------  dealStudentClass 处理学员报名
- (void)dealStudentClass
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:signCode forKey:@"signCode"];
    [params setObject:[NSNumber numberWithInt:handFlag] forKey:@"handFlag"];
    [params setObject:handMemo forKey:@"handMemo"];
    [params setObject:signFee forKey:@"signFee"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"dealStudentClass" strParams:signCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"dealStudentClass" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50000) {
            CSAlert(@"成功");
            self.bottomView.hidden = YES;
            self.page = 1;
            [self getStudentClassList];
            [self textViewDidEndEditing:self.explainView];// 刷新时让键盘消失
        }else if (code.intValue == 20000){
            CSAlert(@"失败");
        }else if (code.intValue == 77033){
            CSAlert(@"处理意见为空");
        }else if (code.intValue == 77034){
            CSAlert(@"报名费用为空");
        }else if (code.intValue == 77035){
            CSAlert(@"该报名信息不存在");
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];
    
}


#pragma mark - Keyboard notification

- (void)onKeyboardNotification:(NSNotification *)notification {
    //Reset constraint constant by keyboard height
    if ([notification.name isEqualToString:UIKeyboardWillShowNotification]) {
        CGRect keyboardFrame = ((NSValue *) notification.userInfo[UIKeyboardFrameEndUserInfoKey]).CGRectValue;
        
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-keyboardFrame.size.height);
        
    } else if ([notification.name isEqualToString:UIKeyboardWillHideNotification]) {
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
        
        self.bottomView.hidden = YES;
        self.textViewPlaceholder.hidden = NO;
        self.bottomView.frame = CGRectMake(0, self.view.frame.size.height - 100, APP_VIEW_WIDTH, 100);
        self.explainView.text = @"";
        
    }
    
    //Animate change
    [UIView animateWithDuration:0.8f animations:^{
        [self.view layoutIfNeeded];
    }];
    
}

- (void)dealloc {
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification
                                                  object:nil];
    
}



#pragma mark ----- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
