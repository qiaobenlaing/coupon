//
//  SettlementViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "SettlementViewController.h"
#import "SVProgressHUD.h"

@interface SettlementViewController ()<UITableViewDataSource, UITableViewDelegate,ZBarReaderDelegate>

{
    BOOL isSubmit;
    BOOL isSelect;
    
//    BOOL isChange;
    BOOL isListSelect;
    
    int seleTag;
    int moneyValue;
    int inputValue;
    
    NSTimer * timer;
    UIImageView* _line;
    BOOL upOrdown;
    int num;
    
    
}


@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic, strong) NSMutableArray * selectListAry;
@property (nonatomic, strong) UITextField * inputField;

@end

@implementation SettlementViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.selectListAry = [@[]mutableCopy];
    
    isSubmit = NO;
//    isChange = NO;
    moneyValue = 0;
    seleTag = 0;
    isSelect = NO;
    inputValue = -1;
    [self setNavBackItem];
    [self setNavTitle:@"结算"];
    
    [self setViewUp];
    
    
}

- (void)setViewUp
{
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT) style:UITableViewStyleGrouped];
    _baseView.rowHeight = 40;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    [self.view addSubview:self.baseView];
    
    
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.orderProductList.count == 0) {
        return 0;
    }else{
        return self.orderProductList.count + 3;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row < self.orderProductList.count) {
        static NSString * cell_id = @"OrderListViewCell";
        OrderListViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[OrderListViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setCellValue:self.productList[indexPath.row]];
        
        return cell;
        
    }else if (indexPath.row == self.orderProductList.count){
        static NSString * cell_id = @"AllChooseViewCell";
        AllChooseViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[AllChooseViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        return cell;
    }else if (indexPath.row == self.orderProductList.count + 1){
        static NSString * cell_id = @"MoneyViewCell";
        MoneyViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[MoneyViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;

        cell.moneyLB.text = [NSString stringWithFormat:@"%d元", moneyValue];
        
        
        
        return cell;
        
    }else{
        static NSString * cell_id = @"InputViewCell";
        InputViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[InputViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;

        self.inputField = [[UITextField alloc] initWithFrame:CGRectMake(70, cell.frame.size.height / 6, cell.frame.size.width - 100, cell.frame.size.height / 6 * 4)];
        _inputField.borderStyle = UITextBorderStyleRoundedRect;
        _inputField.backgroundColor = APP_VIEW_BACKCOLOR;
        _inputField.keyboardType = UIKeyboardTypeDecimalPad;
        [cell.contentView addSubview:self.inputField];
        
        UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
        [topView setBarStyle:UIBarStyleBlackTranslucent];
        
        UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(2, 5, 50, 25);
        [btn setTitle:@"隐藏" forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
        [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
        UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
        NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
        [topView setItems:buttonsArray];
        [_inputField setInputAccessoryView:topView];
        
        
        
        
        return cell;
    }
    
    
}
- (void)dismissKeyB {
    
    [self.inputField resignFirstResponder];
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    if (indexPath.row < self.orderProductList.count) {

        OrderListViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        
        
        moneyValue = moneyValue + [cell didClickCell:cell.isSelect];
        
        NSIndexPath *orderIndexPath = [NSIndexPath indexPathForRow:self.orderProductList.count inSection:0];
        AllChooseViewCell *allcell = [tableView cellForRowAtIndexPath:orderIndexPath];
        allcell.hokImage.image = [UIImage imageNamed:@"radio_no.png"];
        
        [self.baseView reloadData];
        
        
   
    }else if (indexPath.row == self.orderProductList.count){
        isSelect = !isSelect;
        if (isSelect) {
            for (int i = 0; i<self.orderProductList.count; i++) {
                NSIndexPath *orderIndexPath = [NSIndexPath indexPathForRow:i inSection:0];
                
                OrderListViewCell *cell = [tableView cellForRowAtIndexPath:orderIndexPath];
                
                [cell didClickCell:NO];
                
            }
        }else {
            for (int i = 0; i<self.orderProductList.count; i++) {
                NSIndexPath *orderIndexPath = [NSIndexPath indexPathForRow:i inSection:0];
                
                OrderListViewCell *cell = [tableView cellForRowAtIndexPath:orderIndexPath];
                
                [cell didClickCell:YES];
                
            }
        }
        
        
        
        AllChooseViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
        
        moneyValue = [cell didClickCell:[self.actualOrderAmount intValue]];

        
        
    }
    
    [self.baseView reloadData];
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 40;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 60;
    
}
//
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UILabel * titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
    int number = (int)self.orderProductList.count;
    titleLB.text = [NSString stringWithFormat:@"订单详情 (%d个商品)",number];
    titleLB.font = [UIFont systemFontOfSize:14];
    
    return titleLB;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView * footerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH - 20, 60)];
    int payNumber = [self.payTypeNum intValue];
    if (payNumber == 0 && !isSubmit) {
        
        UIButton * submitBut = [UIButton buttonWithType:UIButtonTypeCustom];
        submitBut.frame = CGRectMake(10, 15,APP_VIEW_WIDTH - 20 , 50);
        [submitBut setTitle:@"提交" forState:UIControlStateNormal];
        [submitBut addTarget:self action:@selector(submitButClick:) forControlEvents:UIControlEventTouchUpInside];
        submitBut.backgroundColor = [UIColor redColor];
        [footerView addSubview:submitBut];
        
    }else{
        
        if (isSubmit) {
            UIButton * submitBut = [UIButton buttonWithType:UIButtonTypeCustom];
            submitBut.frame = CGRectMake(10, 15,APP_VIEW_WIDTH - 20 , 50);
            [submitBut setTitle:@"提交" forState:UIControlStateNormal];
            [submitBut addTarget:self action:@selector(submitButClick:) forControlEvents:UIControlEventTouchUpInside];
            submitBut.backgroundColor = [UIColor redColor];
            [footerView addSubview:submitBut];
            
        }else{
            
            
            UIButton * waitBut = [UIButton buttonWithType:UIButtonTypeCustom];
            waitBut.frame = CGRectMake(10, 0, (footerView.frame.size.width - 20) / 2, 50);
            [waitBut setTitle:@"等待支付" forState:UIControlStateNormal];
            waitBut.backgroundColor = [UIColor grayColor];
            [footerView addSubview:waitBut];
            
            UIButton * upPriceBut = [UIButton buttonWithType:UIButtonTypeCustom];
            upPriceBut.frame = CGRectMake(footerView.frame.size.width - (footerView.frame.size.width - 20) / 2 + 10, 0, (footerView.frame.size.width - 20) / 2, 50);
            [upPriceBut setTitle:@"修改价格" forState:UIControlStateNormal];
            [upPriceBut addTarget:self action:@selector(upPriceButCilck:) forControlEvents:UIControlEventTouchUpInside];
            upPriceBut.backgroundColor = [UIColor redColor];
            [footerView addSubview:upPriceBut];
            
        }
    
    }
    return footerView;
   
}




#pragma mark ----- 按钮触发方法
// 提交
- (void)submitButClick:(UIButton *)sender
{
    
    inputValue = [(NSNumber *)self.inputField.text intValue];
    
    if (moneyValue == 0) {
        UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"请选中菜品" message:nil delegate:nil cancelButtonTitle:nil otherButtonTitles:nil, nil];
        [alertView show];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [alertView dismissWithClickedButtonIndex:0 animated:YES];
        });
        
    }else{
        
        isSubmit = NO;
        
        [self submitEnd];
        
        [self.baseView reloadData];
        
    }
    
    
    
    
}
// 修改价格
- (void)upPriceButCilck:(UIButton *)sender
{
    isSubmit = YES;
//    isChange = NO;
    
    inputValue = -1;
    [self.baseView reloadData];
    
}

#pragma mark ---- submitEnd

// 提交结算
- (void)submitEnd
{
    
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.orderCode forKey:@"orderCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"submitEnd" strParams:self.orderCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    if (inputValue == -1) {
        [params setObject:[NSNumber numberWithInt:moneyValue] forKey:@"actualOrderAmount"];
        
        
    }else{
        
        [params setObject:[NSNumber numberWithInt:inputValue] forKey:@"actualOrderAmount"];
        
    }
    
    [params setObject:self.orderProductList forKey:@"orderProductList"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"submitEnd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)  {
        [SVProgressHUD dismiss];
        
        NSNumber * code = (NSNumber *)responseObject[@"code"];
        if ([code isEqualToNumber:[NSNumber numberWithInt:50000]]) {
            
            UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"提交成功" message:@"等待用户支付" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil, nil];
            [alertView show];
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [alertView dismissWithClickedButtonIndex:0 animated:YES];
            });
            self.payTypeNum = [NSNumber numberWithBool:1];
            [self.baseView reloadData];

        }else if ([code isEqualToNumber:[NSNumber numberWithInt:20000]]|| [code isEqualToNumber:[NSNumber numberWithInt:60541]]){
            CSAlert(@"提交失败");
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        
    }];


}




#pragma mark ------- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
