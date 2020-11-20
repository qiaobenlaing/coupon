//
//  DistributionPlanViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "DistributionPlanViewController.h"
#import "BMSQ_DineSetUpViewController.h"
@interface DistributionPlanViewController ()<UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate>
{
    int number;
}


@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSIndexPath * indexPath;

@property (nonatomic, strong) NSMutableArray * upNewAry;
@property (nonatomic, strong) NSNumber * deliveryId;

@end

@implementation DistributionPlanViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    number = (int)self.deliveryList.count;
    self.upNewAry = [@[] mutableCopy];
    [self setNavBackItem];
    [self setNavTitle:@"配送方案"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    [self customRightBtn];
    [self setViewUp];
    
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setTitle:@"完成" forState:UIControlStateNormal];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(completeButClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}


- (void)setViewUp
{
    
    UIButton * addBut = [UIButton buttonWithType:UIButtonTypeCustom];
    addBut.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
    [addBut addTarget:self action:@selector(addButClick:) forControlEvents:UIControlEventTouchUpInside];
    UIImageView * imageView = [[UIImageView alloc] initWithFrame:CGRectMake(addBut.frame.size.width / 2 - 20, 0, 40, addBut.frame.size.height)];
    imageView.image = [UIImage imageNamed:@"right_add"];
    [addBut addSubview:imageView];
    addBut.backgroundColor = [UIColor grayColor];
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    _baseView.rowHeight = 90;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    _baseView.tableFooterView = addBut;
    _baseView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    [self.view addSubview:self.baseView];
    
    
    
    
    
}

#pragma mark --- UITableViewDataSource, UITableViewDelegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.deliveryList.count == 0) {
        return 0;
    }
    return self.deliveryList.count;

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"DistributionPlanViewCell";
    DistributionPlanViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[DistributionPlanViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    NSNumber * leftStr = self.deliveryList[indexPath.row][@"deliveryDistance"];
    int leftNum = [leftStr intValue] / 1000;
    NSNumber * requireMoneyNum = self.deliveryList[indexPath.row][@"requireMoney"];
    NSNumber * deliveryFeeNum = self.deliveryList[indexPath.row][@"deliveryFee"];
    NSNumber * deliveryId = self.deliveryList[indexPath.row][@"deliveryId"];
    
    cell.headerLB.text = [NSString stringWithFormat:@"方案 %@:",deliveryId];
    
    cell.rangeField.tag = 1000;
    cell.riseField.tag = 2000;
    cell.deliveryField.tag = 3000;
    
    cell.rangeField.delegate = self;
    cell.riseField.delegate = self;
    cell.deliveryField.delegate = self;
    
    [cell.rangeField addTarget:self action:@selector(rangeFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    [cell.riseField addTarget:self action:@selector(riseFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    [cell.deliveryField addTarget:self action:@selector(deliveryFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    
    
    
    if (leftNum == 0 && [requireMoneyNum isEqualToNumber:[NSNumber numberWithInt:0]] && [deliveryFeeNum isEqualToNumber:[NSNumber numberWithInt:0]]) {
        cell.rangeField.text = @"";
        cell.riseField.text = @"";
        cell.deliveryField.text = @"";
    }else{
        cell.rangeField.text = [NSString stringWithFormat:@"%d",leftNum];
        cell.riseField.text = [NSString stringWithFormat:@"%@", requireMoneyNum];
        cell.deliveryField.text = [NSString stringWithFormat:@"%@", deliveryFeeNum];
       
    }
    
    return cell;
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * dic = self.deliveryList[indexPath.row];
    self.deliveryId = dic[@"deliveryId"];
    
    [self.deliveryList removeObject:dic];
    
    [tableView  deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationRight];
    
    [self delShopDelivery];
}



- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}
#pragma mark ---- 删除配送方案
- (void)delShopDelivery
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.deliveryId forKey:@"deliveryId"];
    NSString* vcode = [gloabFunction getSign:@"delShopDelivery" strParams:[NSString stringWithFormat:@"%@", self.deliveryId]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"delShopDelivery" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        
        NSNumber * code = (NSNumber *)responseObject[@"code"];
        
        if ([code isEqualToNumber:[NSNumber numberWithInt:50000]]) {
            
            CSAlert(@"修改成功");
            
        }else if ([code isEqualToNumber:[NSNumber numberWithInt:20000]]){
            
             CSAlert(@"修改失败");
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
        CSAlert(@"修改失败");
    }];

    
    
}


#pragma mark ----- addButClick添加
- (void)addButClick:(UIButton *)sender
{
    NSLog(@"添加");
    NSLog(@"%@", self.deliveryList);
    NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithCapacity:0];
    [dic setObject:@"" forKey:@"deliveryId"];
    [dic setObject:[NSNumber numberWithInt:0] forKey:@"deliveryDistance"];
    [dic setObject:[NSNumber numberWithInt:0] forKey:@"requireMoney"];
    [dic setObject:[NSNumber numberWithInt:0] forKey:@"deliveryFee"];
    
    [self.deliveryList addObject:dic];
    
    [self.baseView reloadData];
    
    
}

#pragma mark ---- 完成按钮
- (void)completeButClick:(UIButton *)sender
{
    
    NSLog(@"%@", self.deliveryList);
    
    for (NSDictionary * dic in self.deliveryList) {
        NSMutableDictionary * newDic = [NSMutableDictionary dictionaryWithCapacity:0];
        [newDic setObject:dic[@"deliveryId"] forKey:@"deliveryId"];//配送方案ID
        [newDic setObject:dic[@"deliveryDistance"] forKey:@"deliveryDistance"];//配送范围
        [newDic setObject:dic[@"requireMoney"] forKey:@"requireMoney"];// 起送价
        [newDic setObject:dic[@"deliveryFee"] forKey:@"deliveryFee"];// 配送费
        [newDic setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
        
        [self.upNewAry addObject:newDic];
    }

    
    [self editShopDeliveryBatch];
    [self.navigationController popViewControllerAnimated:YES];
    
}




#pragma mark ----- 添加，修改配送方案
- (void)editShopDeliveryBatch
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    NSString* vcode = [gloabFunction getSign:@"editShopDeliveryBatch" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [params setObject:self.upNewAry forKey:@"deliveryList"];
    
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"editShopDeliveryBatch" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
         CSAlert(@"修改成功");
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
         CSAlert(@"修改失败");
    }];

    

}


#pragma mark --- 
- (void)rangeFieldChanged:(UITextField *)sender
{
    UITableViewCell * cell = (UITableViewCell *)[sender superview];
    NSIndexPath * indexPath = [self.baseView indexPathForCell:cell];
    NSMutableDictionary * mdic = [NSMutableDictionary dictionaryWithDictionary:self.deliveryList[indexPath.row]];
    
    [self.deliveryList removeObjectAtIndex:indexPath.row];
    int num = [(NSNumber *)sender.text intValue] * 1000;
    [mdic setObject:[NSNumber numberWithInt:num] forKey:@"deliveryDistance"];
    [self.deliveryList insertObject:mdic atIndex:indexPath.row];
    
    
}

- (void)riseFieldChanged:(UITextField *)sender
{
    UITableViewCell * cell = (UITableViewCell *)[sender superview];
    NSIndexPath * indexPath = [self.baseView indexPathForCell:cell];
    NSMutableDictionary * mdic = [NSMutableDictionary dictionaryWithDictionary:self.deliveryList[indexPath.row]];
    [self.deliveryList removeObjectAtIndex:indexPath.row];
    [mdic setObject:sender.text forKey:@"requireMoney"];
    [self.deliveryList insertObject:mdic atIndex:indexPath.row];

}

- (void)deliveryFieldChanged:(UITextField *)sender
{
    UITableViewCell * cell = (UITableViewCell *)[sender superview];
    NSIndexPath * indexPath = [self.baseView indexPathForCell:cell];
     NSMutableDictionary * mdic = [NSMutableDictionary dictionaryWithDictionary:self.deliveryList[indexPath.row]];
    [self.deliveryList removeObjectAtIndex:indexPath.row];
    [mdic setObject:sender.text forKey:@"deliveryFee"];
    [self.deliveryList insertObject:mdic atIndex:indexPath.row];

}
#pragma mark -----  UITextFieldDelegate代理方法实现

//将要开始编辑
//- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
//{
//    
//    return YES;
//}
//开始编辑
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    UITableViewCell * cell = (UITableViewCell *)[textField superview];
    NSIndexPath * indexPath = [self.baseView indexPathForCell:cell];
    
    //设置弹起动画效果的时间
//    NSTimeInterval animationDuration=0.3f;
    //设置执行动画ResizeForKeyboard可以随意写
//    [UIView beginAnimations:@"ResizeForKeyboard" context:nil];
    // 用上面定义的时间间隔作为动画的持续时间
//    [UIView setAnimationDuration:animationDuration];
    //开始编辑时生成新的输入框坐标
    float width = self.view.frame.size.width;
    float height = self.view.frame.size.height;
    if (indexPath.row < 2) {
        CGRect rect=CGRectMake(0,-10,width,height);
        //新坐标赋值给view
        self.view.frame=rect;
    }else{
        //新的view的坐标 只改变 Y 的值
        CGRect rect=CGRectMake(0,-230,width,height);
        //新坐标赋值给view
        self.view.frame=rect;
    }
  
    // 提交动画，也就是结束动画
//    [UIView commitAnimations];
}

//将要结束编辑
//- (BOOL)textFieldShouldEndEditing:(UITextField *)textField
//{
//    return YES;
//}

//完成编辑
- (void)textFieldDidEndEditing:(UITextField *)textField
{
    //提前设置动画效果的时间
//    NSTimeInterval animationDuration=0.4f;
//    [UIView beginAnimations:@"ResizeForKeyboard" context:nil];
    // 用上面定义的时间间隔作为动画的持续时间
//    [UIView setAnimationDuration:animationDuration];
    //还原view的坐标到初始状态
    self.view.frame =CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height);
}


#pragma mark ---- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
