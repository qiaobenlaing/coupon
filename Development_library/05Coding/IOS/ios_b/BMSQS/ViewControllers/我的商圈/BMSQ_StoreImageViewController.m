//
//  BMSQ_StoreImageViewController.m
//  BMSQS
//
//  Created by lxm on 15/8/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_StoreImageViewController.h"
#import "UIImageView+WebCache.h"
#import "LEFlowView.h"
#import "EGOImageView.h"
#import <QuartzCore/QuartzCore.h>
#import "BMSQ_StoreQrcodeViewController.h"

@interface BMSQ_StoreImageViewController ()

@end

@implementation BMSQ_StoreImageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self.navigationItem setTitle:@"店铺形象"];
    // Do any additional setup after loading the view from its nib.
    if ([_tableView respondsToSelector:@selector(setSeparatorInset:)]) {
        [_tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    
    if ([_tableView respondsToSelector:@selector(setLayoutMargins:)]) {
        [_tableView setLayoutMargins:UIEdgeInsetsZero];
    }
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
    aFlowView.delegate = nil;
    aFlowView.dataSource = nil;
    [aFlowView stopTimer:YES];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(aFlowView){
        aFlowView.delegate = self;
        aFlowView.dataSource = self;
        [aFlowView setTimer:3.f];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 4;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.row==0)
        return 70;
    else if(indexPath.row==1)
        return 44.f;
    else if(indexPath.row==2)
        return 267.f;
    else if(indexPath.row==3)
        return 100.f;
    return 44.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = @"TableViewCell";
    if(indexPath.row==0)
        cellIdentifier = @"TopTableViewCell";
    else if(indexPath.row==1)
        cellIdentifier = @"QRCodeTableViewCell";
    else if(indexPath.row==2)
        cellIdentifier = @"ImageTableViewCell";
    else if(indexPath.row==3)
        cellIdentifier = @"DesTableViewCell";
    
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
    }else{
        
    }
    NSDictionary *dic = [self.shopAllMsg objectForKey:@"shopInfo"];
    
    if(indexPath.row==0){
        NSString *imgStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[dic objectForKey:@"logoUrl"]];
        
        UIImageView *imageView = (UIImageView *)[setCell viewWithTag:100];
        [imageView sd_setImageWithURL:[NSURL URLWithString:imgStr]];
        
        UILabel *nameLabel = (UILabel *)[setCell viewWithTag:101];
        nameLabel.text =  [dic objectForKey:@"shopName"];
        UILabel *streetLabel = (UILabel *)[setCell viewWithTag:102];
        streetLabel.text =  [dic objectForKey:@"street"];
        UILabel *telLabel = (UILabel *)[setCell viewWithTag:103];
        telLabel.text =  [dic objectForKey:@"tel"];
        
        
    }else if(indexPath.row==1){
        setCell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    }else if(indexPath.row==2){
        [setCell.contentView setBackgroundColor:[UIColor colorWithHexString:@"0xf0eff5"]];
        aFlowView = (LEFlowView *)[setCell viewWithTag:300];
        aFlowView.layer.cornerRadius =4;
        aFlowView.delegate = self;
        aFlowView.dataSource = self;
        [aFlowView setTimer:3.f];
        [aFlowView addClickViewAbility];
        
        UIPageControl *advertisPageControl = [[UIPageControl alloc] initWithFrame:CGRectMake(0, aFlowView.frame.size.height - 20, aFlowView.frame.size.width, 20)];
        aFlowView.pageControl = advertisPageControl;
        [aFlowView addSubview:advertisPageControl];
        
        bFlowView = (LEFlowView *)[setCell viewWithTag:301];
        bFlowView.delegate = self;
        bFlowView.dataSource = self;
        bFlowView.backgroundColor = [UIColor clearColor];
        [bFlowView addClickViewAbility];
    }else if(indexPath.row==3){
        UITextView *textView = (UITextView *)[setCell viewWithTag:400];
        textView.text = [dic objectForKey:@"shortDes"];
    }
    
    
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
 
    if(indexPath.row==1){
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"DecorationShop" bundle:nil];
        BMSQ_StoreQrcodeViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_StoreQrcode"];
        vc.shopAllMsg = self.shopAllMsg;
        [self.navigationController pushViewController:vc animated:YES];

    }
}


- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 0.1f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([cell respondsToSelector:@selector(setSeparatorInset:)]) {
        [cell setSeparatorInset:UIEdgeInsetsZero];
    }
    if ([cell respondsToSelector:@selector(setLayoutMargins:)]) {
        [cell setLayoutMargins:UIEdgeInsetsZero];
    }
}

#pragma mark - PagedFlowViewDelegate Methods
- (void)viewClickedWithIndex:(int)viewIndex
{
    NSLog(@"viewClickedWithIndex!");
    if (IsNOTNullOrEmptyOfArray(self.shopAllMsg[@"shopDecoration"]))
    {
        NSArray *imgArray = self.shopAllMsg[@"shopDecoration"][@"decoration"];
        if (IsNOTNullOrEmptyOfArray(imgArray))
        {
            if(viewIndex < [imgArray count])
            {
                /*进入图片展示vc*/
                //            NSInteger count = imgArray.count;
                //            NSMutableArray *photos = [NSMutableArray arrayWithCapacity:count];
                //            for (int i = 0; i<count; i++) {
                //                MJPhoto *photo = [[MJPhoto alloc] init];
                //                photo.url = [NSURL URLWithString:[imgArray objectAtIndex:i]]; // 图片路径
                //                //photo.srcImageView = self.view.subviews[i]; // 来源于哪个UIImageView
                //                [photos addObject:photo];
                //            }
                //
                //            // 2.显示相册
                //            MJPhotoBrowser *browser = [[MJPhotoBrowser alloc] init];
                //            browser.currentPhotoIndex = viewIndex; // 弹出相册时显示的第一张图片是？
                //            browser.photos = photos; // 设置所有的图片
                //            [browser show];
            }
            
        }
    }
}

- (CGSize)sizeForPageInFlowView:(LEFlowView *)flowView
{
    return [flowView bounds].size;
}
#pragma mark - PagedFlowViewDataSource Methods
//返回显示View的个数
- (NSInteger)numberOfPagesInFlowView:(LEFlowView *)flowView
{
    NSInteger ret = 0;
    if(flowView.tag==300){
        if (IsNOTNullOrEmptyOfArray(self.shopAllMsg[@"shopDecoration"]))
        {
            NSArray *imgArray = self.shopAllMsg[@"shopDecoration"][@"decoration"];
            if (IsNOTNullOrEmptyOfArray(imgArray))
            {
                ret = [imgArray count];
            }
        }
    }else
        if(flowView.tag==301){
        NSArray *imgArray = self.shopAllMsg[@"shopCoupon"];
        if (IsNOTNullOrEmptyOfArray(imgArray))
        {
            ret = ceil([imgArray count]/2.0);
            NSLog(@"%d",ret);
        }
    }
    return ret;
}
//返回给某列使用的View
- (UIView *)flowView:(LEFlowView *)flowView cellForPageAtIndex:(NSInteger)index
{
    UIView *ret = nil;
    if(flowView.tag==300){
        if (IsNOTNullOrEmptyOfArray(self.shopAllMsg[@"shopDecoration"]))
        {
            NSArray *imgArray = self.shopAllMsg[@"shopDecoration"][@"decoration"];
            if (IsNOTNullOrEmptyOfArray(imgArray))
            {
                NSString *imgStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME, [imgArray objectAtIndex:index][@"imgUrl"]];
                if(IsNOTNullOrEmptyOfNSString(imgStr)){
                    EGOImageView *aViewImage = [[EGOImageView alloc] init];
                    [aViewImage setFrame:[flowView bounds]];
                    aViewImage.backgroundColor = [UIColor clearColor];
                    //                [aViewImage setContentMode:UIViewContentModeScaleToFill];
                    aViewImage.layer.cornerRadius = 4;
                    aViewImage.layer.borderWidth = 0;
                    aViewImage.aViewContentMode = UIViewContentModeScaleToFill;
                    [aViewImage setImageURL:[NSURL URLWithString:imgStr]];
                    ret = aViewImage;
                }
            }
        }
    }else
        if(flowView.tag==301){
        NSArray *imgArray = self.shopAllMsg[@"shopCoupon"];
            ret = [[UIView alloc] initWithFrame:flowView.frame];
        if (IsNOTNullOrEmptyOfArray(imgArray))
        {
            
            if (index*2 < imgArray.count) {
               NSDictionary *rowsData1 = (NSDictionary *)[imgArray objectAtIndex:index*2];
                UIImageView *imageView1 = [[UIImageView alloc] initWithFrame:CGRectMake(5, 0, flowView.frame.size.width/2-10, flowView.frame.size.height)];
                imageView1.image = [UIImage imageNamed:@"me_优惠券"];
                imageView1.backgroundColor = [UIColor clearColor];
                [ret addSubview:imageView1];
                
                UIImageView *imageView2 = [[UIImageView alloc] initWithFrame:CGRectMake(80, 25, 50, 16)];
                imageView2.image = [UIImage imageNamed:@"me_抢"];
                imageView2.backgroundColor = [UIColor clearColor];
                [imageView1 addSubview:imageView2];
                
                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"¥%@",rowsData1[@"insteadPrice"]]];
    
                [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:11.f] range:NSMakeRange(0, 1)];
                [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:20.f] range:NSMakeRange(1, str.length-1)];
                
                UILabel *label1 = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, 100, 20)];
                label1.textColor = [UIColor whiteColor];
                label1.attributedText = str;

                [imageView1 addSubview:label1];
                
                UILabel *label2 = [[UILabel alloc] initWithFrame:CGRectMake(80, 10, 150, 11)];
                label2.textColor = [UIColor whiteColor];
                label2.font = [UIFont systemFontOfSize:11.f];
                label2.text = [NSString stringWithFormat:@"满%@可使用",rowsData1[@"availablePrice"]];
                [imageView1 addSubview:label2];
                
                UIImageView *imageView3 = [[UIImageView alloc] initWithFrame:CGRectMake(0,48, flowView.frame.size.width/2, 2)];
                imageView3.image = [UIImage imageNamed:@"me_优惠券分割线"];
                imageView3.backgroundColor = [UIColor clearColor];
                [imageView1 addSubview:imageView3];
                
                UILabel *label3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 50, flowView.frame.size.width/2, 11)];
                label3.textColor = [UIColor whiteColor];
                label3.font = [UIFont systemFontOfSize:9.f];
                label3.textAlignment = NSTextAlignmentCenter;
                label3.text = [NSString stringWithFormat:@"最后领取时间%@",rowsData1[@"endTakingTime"]];;
                [imageView1 addSubview:label3];
            }
            if (index*2+1 < imgArray.count) {
                NSDictionary *rowsData2 = (NSDictionary *)[imgArray objectAtIndex:index*2+1];
                UIImageView *imageView1 = [[UIImageView alloc] initWithFrame:CGRectMake(flowView.frame.size.width/2+5, 0, flowView.frame.size.width/2-10, flowView.frame.size.height)];
                imageView1.image = [UIImage imageNamed:@"me_优惠券"];
                imageView1.backgroundColor = [UIColor clearColor];
                [ret addSubview:imageView1];
                
                UIImageView *imageView2 = [[UIImageView alloc] initWithFrame:CGRectMake(80, 25, 50, 16)];
                imageView2.image = [UIImage imageNamed:@"me_抢"];
                imageView2.backgroundColor = [UIColor clearColor];
                [imageView1 addSubview:imageView2];

                NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"¥%@",rowsData2[@"insteadPrice"]]];
                
                [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:11.f] range:NSMakeRange(0, 1)];
                [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:20.f] range:NSMakeRange(1, str.length-1)];
                
                UILabel *label1 = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, 100, 20)];
                label1.textColor = [UIColor whiteColor];
                label1.attributedText = str;
                
                [imageView1 addSubview:label1];
                
                UILabel *label2 = [[UILabel alloc] initWithFrame:CGRectMake(80, 10, 150, 11)];
                label2.textColor = [UIColor whiteColor];
                label2.font = [UIFont systemFontOfSize:11.f];
                label2.text = [NSString stringWithFormat:@"满%@可使用",rowsData2[@"availablePrice"]];
                [imageView1 addSubview:label2];
                
                
                UIImageView *imageView3 = [[UIImageView alloc] initWithFrame:CGRectMake(0,48, flowView.frame.size.width/2, 2)];
                imageView3.image = [UIImage imageNamed:@"me_优惠券分割线"];
                imageView3.backgroundColor = [UIColor clearColor];
                [imageView1 addSubview:imageView3];

                
                UILabel *label3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 50, flowView.frame.size.width/2, 11)];
                label3.textColor = [UIColor whiteColor];
                label3.font = [UIFont systemFontOfSize:9.f];
                label3.textAlignment = NSTextAlignmentCenter;
                label3.text = [NSString stringWithFormat:@"最后领取时间%@",rowsData2[@"endTakingTime"]];;
                [imageView1 addSubview:label3];

                
            }
        }
    }
    return ret;
}


@end
